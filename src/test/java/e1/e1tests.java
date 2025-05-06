package e1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import tum.i2.cma.*;

public class e1tests {
    public record CMaTriple(CMa machine, int[] stack, int sp) {} // Reduce boilerplate

    CMaTriple setup_run_machine(String pathname) {
        String full_path = String.format("./src/test/java/e1/bytecode/%s", pathname);
        try {
            CMa machine = Helpers.fromCMaCodeFile(full_path);
            machine.run();
            return new CMaTriple(machine, machine.getStack(), machine.getSP());
        } catch(IOException e) {
            String error = String.format("Something went wrong while reading from %s", full_path);
            fail(error);
            return null; // Required for compilation, never reached...
        }
    }

    // Basic Tests for instructions
    @Test
    void loadcTestSimple() {
        String pathname = "loadcTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 2);
        assertEquals(machine_t.stack[0], 1337);
        assertEquals(machine_t.stack[1], 777);
        assertEquals(machine_t.stack[2], 555);
        machine_t.machine.printStack();
    }

    @Test
    void addTestSimple() {
        String pathname = "addTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[0], 10 + 5);
        machine_t.machine.printStack();
    }

    @Test
    void subTestSimple() {
        String pathname = "subTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[0], 10 - 5);
        machine_t.machine.printStack();
    }

    @Test
    void mulTestSimple() {
        String pathname = "mulTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[0], 10 * 5);
        machine_t.machine.printStack();
    }

    @Test
    void divTestSimple() {
        String pathname = "divTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[0], 10 / 5);
        machine_t.machine.printStack();
    }

    @Test
    void modTestSimple() {
        String pathname = "modTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[0], 10 % 5);
        machine_t.machine.printStack();
    }
    
    @Test
    void andTestSimple() {
        String pathname = "andTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp], 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], 0);
        assertEquals(machine_t.stack[machine_t.sp - 2], 0);
        assertEquals(machine_t.stack[machine_t.sp - 3], 0);
        machine_t.machine.printStack();
    }

    @Test
    void orTestSimple() {
        String pathname = "orTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp], 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], 1);
        assertEquals(machine_t.stack[machine_t.sp - 2], 1);
        assertEquals(machine_t.stack[machine_t.sp - 3], 0);
        machine_t.machine.printStack();
    }

    @Test
    void xorTestSimple() {
        String pathname = "xorTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp], 0);
        assertEquals(machine_t.stack[machine_t.sp - 1], 1);
        assertEquals(machine_t.stack[machine_t.sp - 2], 1);
        assertEquals(machine_t.stack[machine_t.sp - 3], 0);
        machine_t.machine.printStack();
    }

    @Test
    void eqTestSimple() {
        String pathname = "eqTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 == 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (1 == 0) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void neqTestSimple() {
        String pathname = "neqTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 != 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (5 != 0) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void leTestSimple() {
        String pathname = "leTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp - 3], (0 < 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 2], (5 < 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 < 5) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (5 < 5) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void leqTestSimple() {
        String pathname = "leqTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp - 3], (0 <= 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 2], (5 <= 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 <= 5) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (5 <= 5) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void grTestSimple() {
        String pathname = "grTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp - 3], (0 > 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 2], (5 > 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 > 5) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (5 > 5) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void geqTestSimple() {
        String pathname = "geqTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 3);
        assertEquals(machine_t.stack[machine_t.sp - 3], (0 >= 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 2], (5 >= 0) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp - 1], (0 >= 5) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (5 >= 5) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void notTestSimple() {
        String pathname = "notTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], (!(5 > 0)) ? 1 : 0);
        assertEquals(machine_t.stack[machine_t.sp], (!(0 > 0)) ? 1 : 0);
        machine_t.machine.printStack();
    }

    @Test
    void negTestSimple() {
        String pathname = "negTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 1);
        assertEquals(machine_t.stack[machine_t.sp - 1], -5);
        assertEquals(machine_t.stack[machine_t.sp], -0);
        machine_t.machine.printStack();
    }

    @Test
    void allocTestSimple() {
        String pathname = "allocTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 9);
        machine_t.machine.printStack();
    }

    @Test
    void storeTestSimple() {
        String pathname = "storeTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 10);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
        assertEquals(machine_t.stack[5], 1337);
        machine_t.machine.printStack();
    }

    @Test
    void storeaTestSimple() {
        String pathname = "storeaTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 10);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
        assertEquals(machine_t.stack[5], 1337);
        machine_t.machine.printStack();
    }

    @Test
    void loadTestSimple() {
        String pathname = "loadTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 14);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
        machine_t.machine.printStack();
    }

    @Test
    void loadaTestSimple() {
        String pathname = "loadaTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        machine_t.machine.printStack();
        assertEquals(machine_t.sp, 14);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
    }

    @Test
    void popTestSimple() {
        String pathname = "popTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);
    
        assertEquals(machine_t.sp, 0);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
        machine_t.machine.printStack();
    }

    @Test
    void jumpTestSimple() {
        String pathname = "jumpTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 10);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
        machine_t.machine.printStack();
    }

    @Test
    void jumpzTestSimple() {
        String pathname = "jumpzTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        machine_t.machine.printStack();
        assertEquals(machine_t.sp, 10);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
    }

    @Test
    void jumpiTestSimple() {
        String pathname = "jumpiTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        machine_t.machine.printStack();
        assertEquals(machine_t.sp, 10);
        assertEquals(machine_t.stack[machine_t.sp], 1337);
    }

    @Test
    void dupTestSimple() {
        String pathname = "dupTestSimple.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 1);
        assertEquals(machine_t.stack[machine_t.sp], 1);
        machine_t.machine.printStack();
    }

    @Test
    void factorial() {
        String pathname = "factorial.cma";
        CMaTriple machine_t = setup_run_machine(pathname);

        assertEquals(machine_t.sp, 0);
        // Apparently no easy way to compute 10! in java, hence magic number
        assertEquals(machine_t.stack[machine_t.sp],3628800);
        machine_t.machine.printStack();
    }
}