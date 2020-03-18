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

const cnst = c => (...values) => c;
const variable = name => (...values) => values[VARIABLES_MAP.get(name)];
const negate = makeOperation("-", x => -x);
const abs = makeOperation("abs",x => Math.abs(x));
const add = makeOperation("+",(x, y) => x + y);
const subtract = makeOperation("-",(x, y) => x - y);
const multiply = makeOperation("*",(x, y) => x * y);
const divide = makeOperation("/",(x, y) => x / y);
const iff = makeOperation("iff",(x, y, z) => x >= 0 ? y : z);

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
const CONSTANTS = new Map([
    ["ONE", "1"],
    ["TWO", "2"]
]);

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
    const getConst = token => cnst(Number.parseInt(CONSTANTS.has(token) ? CONSTANTS.get(token) : token));
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