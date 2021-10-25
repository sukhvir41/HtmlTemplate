package com.github.sukhvir41.core.classgenerator;

import com.github.sukhvir41.core.statements.RenderBodyStatement;

import java.util.*;

import static com.github.sukhvir41.utils.StringUtils.getIndentations;

class TemplateData {

    private final static String BREAK_LINE = "\n";

    private final List<RenderBodyStatement> renderFunctionBody = new ArrayList<>();
    private boolean functionRendered = false;
    private int renderFunctionIndentation = 2;
    // map <Name,Type>
    private final Map<String, String> variables = new TreeMap<>();


    public TemplateData() {
    }

    /**
     * this should only be called once
     *
     * @return function body of the template
     */
    public StringBuilder getRenderFunctionBody() {
        if (functionRendered) {
            throw new IllegalStateException("function is already rendered");
        } else {
            functionRendered = true;
            return this.renderFunctionBody
                    .stream()
                    .map(RenderBodyStatement::getStatement)
                    .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append);
        }
    }

    /**
     * adds the statement to the body. appends the indentation a the start and a break line at the end.
     *
     * @param statement Render Statement
     */
    public void appendToBody(RenderBodyStatement statement) {
        this.renderFunctionBody.add(new InternalStatement(renderFunctionIndentation, statement));
    }

    public void incrementRenderBodyIndentation() {
        ++this.renderFunctionIndentation;
    }

    public void decrementRenderBodyIndentation() {
        --this.renderFunctionIndentation;
    }

    /**
     * expects the name and type won't contain any leading and trailing whitespaces.
     *
     * @param name variable name
     * @param type variable type
     */
    public void addVariable(String type, String name) {
        if (variables.containsKey(name)) {
            String theType = variables.get(name);
            if (!theType.equals(type)) {
                throw new IllegalArgumentException("overriding variable type. " +
                        "Variable name : " + name + ", type set : " + theType + ", overriding type: " + type);
            }
        } else {
            variables.put(name, type);
        }
    }

    /**
     * @return unmodifiable maps of variables. key -> name , value -> type
     */
    public Map<String, String> getVariables() {
        return Collections.unmodifiableMap(this.variables);
    }

    private static class InternalStatement implements RenderBodyStatement {

        private final int indentation;
        private final RenderBodyStatement statement;

        private InternalStatement(int indentation, RenderBodyStatement statement) {
            this.indentation = indentation;
            this.statement = statement;
        }


        @Override
        public String getStatement() {
            return getIndentations(indentation) + statement.getStatement() + BREAK_LINE;
        }
    }

}
