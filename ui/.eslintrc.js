module.exports = {
    env: {
        browser: true,
        es2021: true,
        jest: true
    },
    extends: [
        "plugin:react/recommended",
        "google",
        "eslint:recommended",
        "plugin:react/recommended",
        "plugin:prettier/recommended"
    ],
    parser: "@typescript-eslint/parser",
    parserOptions: {
        ecmaFeatures: {
            jsx: true
        },
        ecmaVersion: "latest"
    },
    plugins: ["react", "@typescript-eslint"],
    rules: {
        "react/react-in-jsx-scope": "off",
        "require-jsdoc":"off",
        "react/no-unescaped-entities": 0
    }
};
