"use strict";
//  HW8 - review

function makeInheritance(object, prototype) {
    object.prototype = Object.create(prototype);
    object.constructor = object;
}

const AbstractOperator = {
    toString: function() {
        return this.arg.join(' ') + ' ' + this.token;
    },
    prefix: function() {
        return '(' + this.token + ' ' + this.arg.map(x => x.prefix()).join(' ') + ')';
    },
    postfix: function() {
        return '(' + this.arg.map(x => x.postfix()).join(' ') + ' ' + this.token + ')';
    },
    evaluate: function(...values) {
        return this.func.apply(this, this.arg.map(x => x.evaluate(...values)));
    },
    diff: function(t) {
        return this.calcDiff.apply(this, this.arg.map(x => x.diff(t)).concat(this.arg));
    }
};

function buildOperator(token, func, calcDiff) {
    function inner(...other) {
        this.arg = other.slice();
        this.token = token;
        this.func = func;
        this.calcDiff = calcDiff;
    }
    makeInheritance(inner, AbstractOperator);
    inner.len = func.length;
    return inner;
}

const AbstractConstAndVariable = {
    toString: function() {
        return `${this.getValue()}`;
    },
    prefix: function () {
        return this.toString();
    },
    postfix: function () {
        return this.toString();
    }
};

function buildConstAndVariable(getValue, evaluate, diff) {
    let inner = Object.create(AbstractConstAndVariable);
    inner.getValue = getValue;
    inner.evaluate = evaluate;
    inner.diff = diff;
    makeInheritance(inner, AbstractConstAndVariable);
    return inner;
}

const AbstractConst = buildConstAndVariable(function() {return this.c;},
    function(...values) {return this.getValue();}, function(t) {return AbstractConst.ZERO;});

const Const = function (c) {
    let inner = Object.create(AbstractConst);
    inner.c = c;
    makeInheritance(inner, AbstractConst);
    return inner;
};

AbstractConst.E = new Const(Math.E);
AbstractConst.ZERO = new Const(0);
AbstractConst.ONE = new Const(1);

const AbstractVariable = buildConstAndVariable(function() {return this.name;},
    function(...values) {return values[this.id];},function(t) {return this.getValue() === t ? AbstractConst.ONE : AbstractConst.ZERO;});

const VARIABLES = ["x", "y", "z"];
const VARIABLES_MAP = new Map([]);
for (let i = 0; i < VARIABLES.length; ++i) {
    VARIABLES_MAP.set(VARIABLES[i], i);
}

const Variable = function(name) {
    let inner = Object.create(AbstractVariable);
    inner.name = name;
    inner.id = VARIABLES_MAP.get(name);
    makeInheritance(inner, AbstractVariable);
    return inner;
};

const Power = buildOperator("pow", (x, y) => Math.pow(x, y),
    (uh, vh, u, v) => {
        const lnU = new Ln(u);
        const lnUH = new Divide(uh, u);
        return new Multiply(new Power(u, v), new Multiply (lnU, v).calcDiff(lnU, lnUH, v, vh));
    });

const makeIt = (uh, u, lnU) => new Multiply(new Divide(uh, u), lnU);

const Log = buildOperator("log", (x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)), (uh, vh, u, v)  => {
    const lnV = new Ln(v);
    const lnU = new Ln(u);
    return new Divide(
        new Subtract(
            makeIt(vh, v, lnU),
            makeIt(uh, u, lnV)
        ),
        new Square(lnU)
    );
});
const Ln = buildOperator("ln", x =>  Math.log(Math.abs(x)),(uh, u) => new Divide(uh, u));
const Negate = buildOperator("negate", x => -x,uh => new Negate(uh));
const Square = buildOperator("sqr", x => x * x, (uh, u) => new Multiply(new Const(2), new Multiply(uh, u)));
const Add = buildOperator("+", (l, r) => l + r, (uh, vh) => new Add(uh, vh));
const Subtract = buildOperator("-", (l, r) => l - r, (uh, vh) => new Subtract(uh, vh));
const Multiply = buildOperator("*", (l, r) => l * r, (uh, vh, u, v) =>
    new Add(new Multiply(uh, v), new Multiply(vh, u)));
const Divide = buildOperator("/", (l, r) => l / r, (uh, vh, u, v) =>
    new Divide(new Subtract(new Multiply(uh, v), new Multiply(vh, u)), new Multiply(v, v)));

const sum = (...arg) => arg.reduce((sum, cur) => sum + cur, 0);
const Sum = buildOperator("sum", sum, (...arg) => new Sum(...arg.slice(0, arg.length / 2)),
    (...arg) => new Sum(...arg));
const sumexp = (...arg) => arg.reduce((sum, curr) => sum + Math.pow(Math.E, curr), 0);
const sumexpDiff = (...arg) => {
    let argh = arg.slice(0, arg.length / 2);
    arg = arg.splice(arg.length / 2);
    for (let i = 0; i < arg.length; ++i) {
        arg[i] = new Multiply(new Power(AbstractConst.E, arg[i]), argh[i]);
    }
    return new Sum(...arg);
};
const Sumexp = buildOperator("sumexp", sumexp, sumexpDiff, (...arg) => new Sumexp(...arg));
const softmax = (...arg) => {
    return Math.pow(AbstractConst.E, arg[0]) / sumexp(...arg);
};
const Softmax = buildOperator("softmax", softmax, (...arg) => {
        const u = arg[arg.length / 2];
        const uh = arg[0];
        const sum = new Sumexp(...arg.slice(arg.length / 2, arg.length));
        const sumh = sumexpDiff(...arg);
        return new Multiply(
            new Divide(
                new Power(AbstractConst.E, u),
                new Square(sum)
            ),
            new Subtract(
                new Multiply(uh, sum),
                sumh
            )
        );
    },
    (...arg) => new Softmax(...arg));

const OPERATIONS = new Map([
    ["+", Add], ["-", Subtract], ["*", Multiply],
    ["/", Divide], ["log", Log], ["ln", Ln],
    ["pow", Power], ["negate", Negate],
    ["softmax", Softmax], ["sumexp", Sumexp]
]);

function ParsingError(message, pos) {
    this.message = message + 'Breaks at index: ' + pos;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.constructor = ParsingError;

function buildError(message) {
    let error = function(pos) {
        ParsingError.call(this, message, pos);
        this.name = "ParserError";
    };
    makeInheritance(error, ParsingError.prototype);
    return error;
}

const MissingClosingBracketError = buildError("Expected closing bracket. ");
const MissingOpeningOrExtraArgument = buildError("There are too mush arguments, so, either missing opening bracket for expression, or found extra arguments. ");
const MissingOpeningBracketError = buildError("Expected opening bracket. ");
const MissingArgumentError = buildError("Expected argument for previous function. ");
const MissingOperationError = buildError("Expected operation between brackets. ");
const ExtraArgumentError = buildError("Expected closing bracket, found extra argument. ");
const IllegalArgumentError = buildError("Illegal argument found. ");
const IllegalOperationError = buildError("Illegal operation found. ");
const UnexpectedEndOfInputError = buildError("Expected expression, found end of input. ");

function test(condition, exception) {
    if (!condition) {
        throw exception;
    }
}

function Source(str) {
    this.specialSymbols = ["(", ")", " "];
    this.pos = 0;
    this.getChar = () => str.charAt(this.pos);
    this.isEndOfInput = () => this.pos >= str.length;
    this.nextChar = () => this.pos++;
    this.checkChar = c => {
        if (c === this.getChar()) {
            this.nextChar();
            return true;
        }
        return  false;
    };
    this.isWhitespace = x => x === ' ';
    this.isConst = token => !Number.isNaN(Number(token));
    this.isVariable = token => VARIABLES_MAP.has(token);
    this.isOperation = token => OPERATIONS.has(token);
    this.getConst = token => new Const(Number.parseFloat(token));
    this.getVariable = token => new Variable(token);
    this.getOperation = token => OPERATIONS.get(token);
    this.skipWhitespace = inv => {while (!this.isEndOfInput() && inv ^ this.isWhitespace(this.getChar())) {this.nextChar();}};
    this.getToken = () => {
        this.skipWhitespace();
        const beg = this.pos;
        while (!this.isEndOfInput() && !this.specialSymbols.includes(this.getChar())) {
            this.nextChar();
        }
        const ans = str.substring(beg, this.pos);
        this.skipWhitespace();
        return ans;
    }
}

function parse(str) {
    let st = [];
    const source = new Source(str);

    while (!source.isEndOfInput()) {
        source.skipWhitespace();
        let beg = source.pos;
        source.skipWhitespace(true);
        const token = str.substring(beg, source.pos);
        if (source.isConst(token)) {
            st.push(source.getConst(token));
        } else if (source.isVariable(token)) {
            st.push(source.getVariable(token));
        } else if (source.isOperation(token)) {
            let operator = OPERATIONS.get(token);
            st.push(new operator(...st.splice(st.length - operator.len)));
        }
    }
    return st[0];
}

function parsePostfix(str) {
    return parsePrefixOrPostfix(str, 'postfix');
}

function parsePrefix(str) {
    return parsePrefixOrPostfix(str, 'prefix');
}

// :NOTE: too many code for parser (it should be up to 50-60 lines)
// :MyNote: fixed, now ~40 lines)

function parsePrefixOrPostfix(str, mode) {
    const source = new Source(str);
    const expression = parseExpression();
    source.skipWhitespace();
    test(source.isEndOfInput(), new MissingOpeningOrExtraArgument(source.pos));
    return expression;
    function parseExpression() {
        source.skipWhitespace();
        test(!source.isEndOfInput(), new UnexpectedEndOfInputError(source.pos));
        if (source.checkChar('(')) {
            let operands = [], operation = (mode === 'prefix' ? parseOperation() : undefined);
            while (!source.checkChar(')')) {
                test(!source.isEndOfInput(), new MissingClosingBracketError(source.pos));
                const token = parseExpression();
                source.skipWhitespace();
                if (source.isOperation(token) && operation === undefined) {
                    operation = source.getOperation(token);
                    test(source.checkChar(')'), new MissingClosingBracketError(source.pos));
                    break;
                } else {
                    test(!source.isOperation(token), new MissingClosingBracketError(source.pos));
                    operands.push(token);
                }
            }
            test(operation !== undefined, new MissingOperationError(source.pos));
            if (operation.len === 0 || operation.len === operands.length) {return new operation(...operands);}
            test(operation.len <= operands.length, new MissingArgumentError(source.pos));
            throw new ExtraArgumentError(source.pos);
        }
        const token = source.getToken();
        if (source.isOperation(token)) {return token;}
        if (source.isVariable(token)) {return source.getVariable(token);}
        if (token.length !== 0 &&  source.isConst(token)) {return source.getConst(token);}
        test(source.isOperation(token), new MissingOpeningBracketError(source.pos - token.length));
        throw new IllegalArgumentError(source.pos - token.length);
    }
    function parseOperation() {
        const token = source.getToken();
        test(source.isOperation(token), new IllegalOperationError(source.pos - token.length));
        return source.getOperation(token);
    }
}
