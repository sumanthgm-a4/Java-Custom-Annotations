import annotations.ImportantString;
import annotations.RunImmediately;
import annotations.VeryImportant;

// Custom Field Annotation
@VeryImportant
public class Cat {

    // Custom Field Annotation
    @ImportantString
    public String name;

    public Cat() {
    }

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void method1() {
        System.out.println("In method-1");
    }

    // Custom Method Annotation
    @RunImmediately(times = 3)
    public void method2() {
        System.out.println("In method-2");
    }

    public void method3() {
        System.out.println("In method-3");
    }

}
