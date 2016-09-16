package com.qualcomm.ftcrobotcontroller.opmodesupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by FIXIT on 15-09-22.
 */

@Retention(RetentionPolicy.RUNTIME)
@DoNotRegister
public @interface Name {

    String value();

    //Optional annotation to allow setting a custom name for an opmode, other than the class name

}
