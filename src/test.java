import edu.rice.hj.api.SuspendableException;

import static edu.rice.hj.Module1.forasync;
import  static edu.rice.hj.Module1.async;
import static edu.rice.hj.Module1.finish;


public class test {
        public static void main(final String[] args) {
            try {
                finish(() -> {
                    forasync(0,100, i ->{
                        System.out.println(i);

                    });
                });
            } catch (SuspendableException e) {
                e.printStackTrace();
            }
        }

}
