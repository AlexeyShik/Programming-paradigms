"use strict";

let OPERATIONS = new Map();

function Operator(token, f, calcDiff, ...other) {
    this.arg = other.slice();
    this.toString = () => `${this.arg.reduce((accumulator, value) => `${accumulator} ${value.toString()}`)} ${token}`;
    this.calcDiff = calcDiff;
    this.evaluate = (...values) => f.apply(this, this.arg.map(x => x.evaluate(...values)));
    this.diff = t => calcDiff.apply(this, this.arg.map(x => x.diff(t)).concat(this.arg));
}

const buildOperator = (token, f, calcDiff)  => {
    function inner(...other) {
        Operator.call(this, token, f, calcDiff, ...other);
        inner.len = f.length;
        OPERATIONS.set(token, inner);
    }
    return inner;
};

function Const(c) {
    this.toString = () => `${c.toString()}`;
    this.diff = x => new Const(0);
    this.evaluate = (...values) => c;
}

function Variable(name) {
    this.toString = () => `${name}`;
    this.diff = x => new Const(name === x ? 1 : 0);
    this.id = VARIABLES_MAP.get(name);
    this.evaluate = (...values) => values[this.id];
}

const Power = buildOperator("pow", (x, y) => Math.pow(x, y),
    (uh, vh, u, v) => {
        const lnU = new Ln(u);
        const lnUH = new Divide(uh, u);
        return new Multiply(new Power(u, v), new Multiply (lnU, v).calcDiff(lnU, lnUH, v, vh));
    });
const Log = buildOperator("log", (x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)), (uh, vh, u, v)  => {
    const lnU = new Ln(u);
    const lnV = new Ln(v);
    const lnUH = lnU.calcDiff(uh, u);
    const lnVH = lnV.calcDiff(vh, v);
    return new Divide(lnV, lnU).calcDiff(lnVH, lnUH, lnV, lnU);
});
const Ln = buildOperator("ln", x =>  Math.log(Math.abs(x)),(uh, u) => new Divide(uh, u));
const Negate = buildOperator("negate", x => -x,uh => new Negate(uh));

const Add = buildOperator("+", (l, r) => l + r, (uh, vh) => new Add(uh, vh));
const Subtract = buildOperator("-", (l, r) => l - r, (uh, vh) => new Subtract(uh, vh));
const Multiply = buildOperator("*", (l, r) => l * r, (uh, vh, u, v) =>
    new Add(new Multiply(uh, v), new Multiply(vh, u)));
const Divide = buildOperator("/", (l, r) => l / r, (uh, vh, u, v) =>
    new Divide(new Subtract(new Multiply(uh, v), new Multiply(vh, u)), new Multiply(v, v)));

const VARIABLES = ["x", "y", "z"];
const VARIABLES_MAP = new Map([]);
for (let i = 0; i < VARIABLES.length; ++i) {
    VARIABLES_MAP.set(VARIABLES[i], i);
}

function parse(str) {
    let pos = 0;
    let st = [];
    const getChar = () => str.charAt(pos);
    const nextChar = () => pos++;
    const skipWhitespace = () => {
        while (!isEndOfInput() && isWhitespace(getChar())) {
            nextChar();
        }
    };
    const isEndOfInput = () => pos >= str.length;
    const isWhitespace = x => x === ' ';
    const isDigit = x => x >= '0' && x <= '9';
    const isConst = token => isDigit(token.charAt(0)) || token.charAt(0) === '-' && isDigit(token.charAt(1));
    const isVariable = token => VARIABLES_MAP.has(token);
    const isOperation = token => OPERATIONS.has(token);
    const getToken = () => {
        skipWhitespace();
        const beg = pos;
        while (!isEndOfInput() && !isWhitespace(getChar())) {
            nextChar();
        }
        return str.substring(beg, pos);
    };
    const getConst = token => new Const(Number.parseInt(token));
    const getOperation = token => OPERATIONS.get(token);
    const getVariable = token => new Variable(token);

    while (!isEndOfInput()) {
        const token = getToken();
        if (isConst(token)) {
            st.push(getConst(token));
        } else if (isVariable(token)) {
            st.push(getVariable(token));
        } else if (isOperation(token)) {
            let operator = getOperation(token);
            st.push(new operator(...st.splice(st.length - operator.len)));
        }
    }
    return st[0];
}
