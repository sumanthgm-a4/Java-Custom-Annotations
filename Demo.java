import java.lang.reflect.Field;
import java.lang.reflect.Method;

import annotations.ImportantString;
import annotations.RunImmediately;
import annotations.VeryImportant;

public class Demo {
    void main() throws Exception {
        
        Cat cat = new Cat("Cat Name");

        if (cat.getClass().isAnnotationPresent(VeryImportant.class)) {
            System.out.println("This class is VERY IMPORTANT");
        } else {
            System.out.println("This class is NOT VERY IMPORTANT");
        }

        for (Method method : cat.getClass().getMethods()) {
            if (method.isAnnotationPresent(RunImmediately.class)) {
                RunImmediately annotation = method.getAnnotation(RunImmediately.class);
                int times = annotation.times();
                for (int i = 0; i < times; i ++)
                    method.invoke(cat);     // Invoke this method on the specified object
            }
        }

        for (Field field : cat.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ImportantString.class)) {
                Object objVal = field.get(cat);     // Get the object's value for that field

                if (objVal instanceof String stringVal) 
                    System.out.println(stringVal);
            }
        }
    }
}