let binary = f => (l, r) => x => f(l(x), r(x));
let unary = f => arg => x => f(arg(x));

let cnst = c => x => c;
let variable = name => x => x;
let negate = unary(arg => -arg);
let add = binary((l, r) => l + r);
let subtract = binary((l, r) => l - r);
let multiply = binary((l, r) => l * r);
let divide = binary((l, r) => l / r);

function example() {
    let expr = subtract(
        multiply(
            cnst(2),
            variable("x")
        ),
        cnst(3)
    );
    console.log(expr(5));
}

function test() {
    let expr = add(
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
}

function parse(str) {
    let pos = 0;
    let ch = undefined;
    let st = [];
    const BINARY = new Map([
        ["+", add],
        ["-", subtract],
        ["*", multiply],
        ["/", divide]
    ]);

    function nextChar() {
        ch = str.charAt(pos++);
    }

    function hasNext() {
        return pos < str.length;
    }

    function skipWhitespace() {
        while (hasNext() && ch === ' ') {
            nextChar();
        }
    }

    function isWhitespace(x) {
        return x === ' ';
    }

    function isDigit(x) {
        return x >= '0' && x <= '9';
    }

    function isConst(token) {
        return isDigit(token.charAt(0)) || token.charAt(0) === '-' && isDigit(token.charAt(1));
    }

    function isVariable(token) {
        return token === "x";
    }

    function getToken() {
        skipWhitespace();
        let token = "";
        while (!isWhitespace(ch)) {
            token += ch;
            if (!hasNext()) {
                break;
            }
            nextChar();
        }
        return token;
    }

    function getOperation(token) {
        return BINARY.get(token);
    }

    while (hasNext()) {
        nextChar();
        const token = getToken();
        if (token.length === 0) {
            break;
        }
        if (isConst(token)) {
            st.push(cnst(Number.parseInt(token)));
        } else if (isVariable(token)) {
            st.push(variable(token));
        } else {
            let r = st.pop(), l = st.pop();
            st.push(getOperation(token)(l, r));
        }
    }
    return st[0];
}

function functionalExpression() {
    console.log(parse("x x 2 - * x * 1 +")(5));
}

functionalExpression();