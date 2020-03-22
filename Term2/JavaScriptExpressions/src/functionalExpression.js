"use strict";

const operation = (func, ...other) => (...values) => {
    let args = [];
    for (let elem of other) {
        args.push(elem(...values));
    }
    return func(...args);
};

let OPERATIONS = new Map([]);
const makeOperation = (name, func) => {
    let f = (...values) => operation(func, ...values);
    OPERATIONS.set(name, f);
    f.argumentsSize = func.length;
    return f;
};

let CONSTANTS = new Map([]);
const makeNumberConst = c => {
    return (...values) => c;
};
const makeConst = (name, c) => {
    let f = makeNumberConst(c);
    CONSTANTS.set(name, f);
    return f;
};

const cnst = c => makeNumberConst(c);
const pi = makeConst("pi", Math.PI);
const e = makeConst("e", Math.E);
const variable = name => (...values) => values[VARIABLES_MAP.get(name)];
const negate = makeOperation("negate", x => -x);
const add = makeOperation("+",(x, y) => x + y);
const subtract = makeOperation("-",(x, y) => x - y);
const multiply = makeOperation("*",(x, y) => x * y);
const divide = makeOperation("/",(x, y) => x / y);
const sum = (...other) => {
    let ans = 0;
    for (const elem of other) {
        ans += elem;
    }
    return ans;
};
const avg = (...other) => sum(...other) / other.length;
const avg5 = makeOperation("avg5", (x1, x2, x3, x4, x5) => avg(x1, x2, x3, x4, x5));
const med = (...other) => {
    other.sort((x, y) => x - y);
    return other[Math.floor(other.length / 2)];
};
const med3 = makeOperation("med3", (x, y, z) => med(x, y, z));

const example = () => {
    const expr = subtract(
        multiply(
            cnst(2),
            variable("x")
        ),
        cnst(3)
    );
    console.log(expr(5));
};

const test = () => {
    const expr = add(
        subtract(
            multiply(
                variable("x"),
                variable("x")
            ),
            multiply(
                cnst(2),
                variable("x")
            )
        ),
        cnst(1)
    );
    for (let x = 0; x <= 10; ++x) {
        console.log(expr(x));
    }
};

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
    const isConst = token => isDigit(token.charAt(0)) || token.charAt(0) === '-' && isDigit(token.charAt(1)) || CONSTANTS.has(token);
    const isVariable = token => VARIABLES_MAP.has(token);
    const isOperation = token => OPERATIONS.has(token);

    const getToken = () => {
        skipWhitespace();
        let beg = pos;
        while (!isEndOfInput() && !isWhitespace(getChar())) {
            nextChar();
        }
        return str.substring(beg, pos);
    };
    const getConst = token => CONSTANTS.has(token) ? CONSTANTS.get(token) : cnst(Number.parseInt(token));
    const getOperation = token => OPERATIONS.get(token);
    const getVariable = token => variable(token);

    while (!isEndOfInput()) {
        const token = getToken();
        if (isConst(token)) {
            st.push(getConst(token));
        } else if (isVariable(token)) {
            st.push(getVariable(token));
        } else if (isOperation(token)) {
            let operator = getOperation(token);
            st.push(operator(...st.splice(st.length - operator.argumentsSize)));
        }
    }
    return st[0];
}

const functionalExpression = () => console.log(parse("x x 2 - * x * 1 +")(5));

example();
test();
functionalExpression();