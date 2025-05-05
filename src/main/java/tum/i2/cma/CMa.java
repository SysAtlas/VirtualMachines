package tum.i2.cma;

import tum.i2.common.VirtualMachine;
public class CMa implements VirtualMachine {
    int sp = -1; // Stack pointer
    int hp = -1; // Heap pointer

    int ip = 0; // Instruction pointer

    static int MEMMAX = 1 << 16;
    int[] stack = new int[MEMMAX];
    int[] heap = new int[MEMMAX];
    CMaInstruction[] instructions;

    // Getters for testing purposes...
    public int getSP() {
        return sp;
    }

    public int[] getStack() {
        return stack;
    }

    // Method for print-based debugging
    public void printStack() {
        for (int i = 0; i <= sp; ++i) {
            String formatted = String.format("Addr %s: %s", i, stack[i]);
            System.out.println(formatted);
        }
    }

    public CMa(CMaInstruction[] instructions) {
        this.instructions = instructions;
    }

    @Override
    public void step() throws RuntimeException {
        this.execute(instructions[ip++]);
        if (sp >= MEMMAX || hp >= MEMMAX) { // After every instruction, check whether the stack and heap pointers are still in a valid state
            throw new RuntimeException("Memory overflow!");
        // FIXME: We currently have no HALT instruction, so we are handling it like this.
        } else if (ip < 0 || ip > instructions.length) {
            throw new RuntimeException("Illegal instruction memory access!");
        }
    }

    @Override
    public int run() {
        while (ip < instructions.length) {
            this.step();
        }
        return 0; // Successful execution
    }

    public void execute(CMaInstruction instruction) {
        // CMaInstructionType enum contains comments,
        // describing where the operations are defined
        switch (instruction.getType()) {
            case LOADC -> {
                this.handle_loadc(instruction.getFirstArg());
            }
            // Arithmetic and logical (as introduced in Simple expressions and assignments)
            case ADD -> {
                this.handle_add();
            }
            case SUB -> {
                this.handle_sub();
            }
            case MUL -> {
                this.handle_mul();
            }
            case DIV -> {
                this.handle_div();
            }
            case MOD -> {
                this.handle_mod();
            }
            case AND -> {
                this.handle_and();
            }
            case OR -> {
                this.handle_or();
            }
            case XOR -> {
                this.handle_xor();
            }
            // Comparison  (as introduced in Simple expressions and assignments)
            case EQ -> {
                this.handle_eq();
            }
            case NEQ -> {
                this.handle_neq();
            }
            case LE -> {
                this.handle_le();
            }
            case LEQ -> {
                this.handle_leq();
            }
            case GR -> {
                this.handle_gr();
            }
            case GEQ -> {
                this.handle_geq();
            }
            // Negation (as introduced in Simple expressions and assignments)
            case NOT -> {
                this.handle_not();
            }
            case NEG -> {
                this.handle_neg();
            }
            // Assignments
            case LOAD -> {
                this.handle_load();
            }
            case STORE -> {
                this.handle_store();
            }
            case LOADA -> {
                this.handle_loada(instruction.getFirstArg());
            }
            case STOREA -> {
                this.handle_storea(instruction.getFirstArg());
            }
            // Statements (as introduced in Statements and Statement Sequences)
            case POP -> {
                this.handle_pop();
            }
            // Conditional and Iterative Statements
            case JUMP -> {
                this.handle_jump(instruction.getFirstArg());
            }
            case JUMPZ -> {
                this.handle_jumpz(instruction.getFirstArg());
            }
            // Introduced in the Switch Statement
            case JUMPI -> {
                this.handle_jumpi(instruction.getFirstArg());
            }
            case DUP -> {
                this.handle_dup();
            }
            // Introduced in Storage Allocation for Variables
            case ALLOC -> {
                this.handle_alloc(instruction.getFirstArg());
            }
            default -> {
                throw new UnsupportedOperationException("Unknown instruction type: " + instruction.getType());
            }
        }
    }

    // Put constant on stack
    private void handle_loadc(int c) {
        ++sp;
        stack[sp] = c;
    }

    // Add top 2 things on stack and pop res
    private void handle_add() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call ADD!");
        }
        int second = stack[sp];
        int first = stack[sp - 1];
        stack[--sp] = first + second;
    }

    private void handle_sub() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call SUB!");
        }
        int second = stack[sp];
        int first = stack[sp - 1];
        stack[--sp] = first - second;
    }

    private void handle_mul() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call MUL!");
        }
        int second = stack[sp];
        int first = stack[sp - 1];
        stack[--sp] = first * second;
    }

    private void handle_div() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call DIV!");
        }
        int second = stack[sp];
        int first = stack[sp - 1];
        if (second == 0) {
            throw new ArithmeticException("Division by zero is not allowed!");
        }
        stack[--sp] = first / second;
    }

    private void handle_mod() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call MOD!");
        }
        int second = stack[sp];
        int first = stack[sp - 1];
        if (second == 0) {
            throw new ArithmeticException("Division by zero is not allowed!");
        }        
        stack[--sp] = first % second;
    }

    /* Implementation is assuming we handle any non-zero 
    values as true and 0 as false and handle the operators as logical operators... */
    private void handle_and() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call AND!");
        }
        int c1 = stack[sp];
        int c2 = stack[sp - 1];
        stack[--sp] = (c1 != 0 && c2 != 0) ? 1 : 0;
    }

    private void handle_or() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call OR!");
        }
        int c1 = stack[sp];
        int c2 = stack[sp - 1];
        stack[--sp] = (c1 != 0 || c2 != 0) ? 1 : 0;
    }

    private void handle_xor() {
        if (sp < 2) {
            throw new RuntimeException("Not enough arguments on the stack to call XOR!");
        }
        int c1 = stack[sp];
        int c2 = stack[sp - 1];
        stack[--sp] = ((c1 != 0 && c2 == 0) || (c1 == 0 && c2 != 0)) ? 1 : 0;
    }
    private void handle_eq() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call EQ!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first == second) ? 1 : 0;
    }
    private void handle_neq() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call NEQ!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first != second) ? 1 : 0;
    }
    private void handle_le() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call LE!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first < second) ? 1 : 0;
    }
    private void handle_leq() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call LEQ!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first <= second) ? 1 : 0;
    }
    private void handle_gr() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call GR!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first > second) ? 1 : 0;
    }
    private void handle_geq() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call GEQ!");
        }
        int second = stack[sp];
        int first = stack[--sp];
        stack[sp] = (first >= second) ? 1 : 0;
    }
    private void handle_not() {
        if (sp < 0) {
            throw new RuntimeException("Not enough arguments on the stack to call NOT!");
        }
        stack[sp] = (stack[sp] != 0) ? 0 : 1;
    }

    private void handle_neg() {
        if (sp < 0) {
            throw new RuntimeException("Not enough arguments on the stack to call NEG!");
        }
        stack[sp] = -stack[sp];
    }

    private void handle_load() {
        if (sp < 0) {
            throw new RuntimeException("Not enough arguments on the stack to call LOAD!");
        }
        int addr = stack[sp];
        if (addr > sp) {
            throw new RuntimeException("Illegal stack memory access!");
        }
        stack[sp] = stack[addr];
    }

    private void handle_store() {
        if (sp < 1) {
            throw new RuntimeException("Not enough arguments on the stack to call STORE!");
        }
        int addr = stack[sp--];
        if (addr > sp) {
            throw new RuntimeException("Illegal stack memory access!");
        }
        stack[addr] = stack[sp];
    }

    private void handle_loada(int addr) {
        if (addr > sp) {
            throw new RuntimeException("Illegal stack memory access!");
        }
        stack[++sp] = stack[addr];
    }

    private void handle_storea(int addr) {
        if (addr > sp || sp < 0) {
            throw new RuntimeException("Illegal stack memory access!");
        }
        stack[addr] = stack[sp];
    }

    private void handle_pop() {
        if (sp == -1) {
            throw new RuntimeException("Nothing to pop from the stack!");
        }
        --sp;
    }

    private void handle_jump(int iaddr) {
        if (iaddr >= instructions.length) {
            throw new RuntimeException("Illegal instruction memory access!");
        }
        ip = iaddr;
    }

    private void handle_jumpz(int iaddr) {
        if (iaddr >= instructions.length) {
            throw new RuntimeException("Illegal instruction memory access!");
        }
        if (stack[sp] == 0) {
            ip = iaddr;
        }
        --sp;
    }

    private void handle_jumpi(int iaddr) {
        if (iaddr >= instructions.length) {
            throw new RuntimeException("Illegal instruction memory access!");
        }
        ip = iaddr + stack[sp];
        --sp;
    }

    private void handle_dup() {
        if (sp == -1) {
            throw new RuntimeException("Nothing to duplicate! Stack is empty!");
        }
        stack[sp + 1] = stack[sp];
        ++sp;
    }

    private void handle_alloc(int k) {
        sp += k;
    }
}
