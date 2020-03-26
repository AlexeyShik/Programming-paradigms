"use strict";

let OPERATIONS = new Map();

function Operator(token, f, calcDiff, ...other) {
    this.arg = other.slice();
    this.toString = () => `${this.arg.reduce((accumulator, value) => `${accumulator} ${value.toString()}`)} ${token}`;
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
    this.arg = c;
    this.diff = x => new Const(0);
    this.evaluate = (...values) => c;
}

function Variable(name) {
    this.toString = () => `${name}`;
    this.name = name;
    this.diff = x => new Const(this.name === x ? 1 : 0);
    this.evaluate = (...values) => values[VARIABLES_MAP.get(name)];
}

const ArcTan = buildOperator("atan", x => Math.atan(x), (uh, u) =>
    new Divide(uh, new Add(new Const(1), new Multiply(u, u))));
const ArcTan2 = buildOperator("atan2", (l, r) => Math.atan2(l, r),
    (vh, u) => (new ArcTan(new Divide(u, vh))));
const Sinh = buildOperator("sinh", x => Math.sinh(x), (uh, u) => new Multiply(new Cosh(u), uh));
const Cosh = buildOperator("cosh", x => Math.cosh(x), (uh, u) => new Multiply(new Sinh(u), uh));

const Negate = buildOperator("negate", x => -x,uh => new Multiply(new Const(-1), uh));
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
    let pos = 0, st = [];
    const skipWhitespace = f => {
        while (pos < str.length && f(str.charAt(pos))) {
            pos++;
        }
    };
    while (pos < str.length) {
        skipWhitespace(x => x === ' ');
        let beg = pos;
        skipWhitespace(x => x !== ' ');
        const token = str.substring(beg, pos);
        if (OPERATIONS.has(token)) {
            let operator = OPERATIONS.get(token);
            st.push(new operator(...st.splice(st.length - operator.len)));
        } else if (VARIABLES_MAP.has(token)) {
            st.push(new Variable(token));
        } else {
            st.push(new Const(Number.parseInt(token)));
        }
    }
    return st[0];
}