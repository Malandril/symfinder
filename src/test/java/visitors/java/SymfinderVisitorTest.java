package visitors.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SymfinderVisitorTest {

    @Test
    void noPackageClass() {
        String className = "MyClass";
        assertEquals(className, SymfinderVisitor.getClassBaseName(className));
    }

    @Test
    void packageClass() {
        String className = "org.MyClass";
        assertEquals(className, SymfinderVisitor.getClassBaseName(className));
    }

    @Test
    void noPackageGenericClass() {
        String className = "MyPair<Integer,String>";
        assertEquals("MyPair", SymfinderVisitor.getClassBaseName(className));
    }

    @Test
    void packageGenericClass() {
        String className = "org.MyPair<Integer,String>";
        assertEquals("org.MyPair", SymfinderVisitor.getClassBaseName(className));
    }
}