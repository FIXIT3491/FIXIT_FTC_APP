package com.qualcomm.ftcrobotcontroller.opmodesupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by FIXIT on 15-08-25.
 */

@Retention(RetentionPolicy.RUNTIME)
@DoNotRegister
public @interface DoNotRegister {

    //Annotation to designate which classes to not register on the FTCOpModeRegister

}
