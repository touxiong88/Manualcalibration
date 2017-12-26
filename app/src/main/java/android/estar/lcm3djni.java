package android.estar;

import com.stereo.ManualCalib.GlobalVariable;

public class lcm3djni
{
    static{

        try {
            if(GlobalVariable.isTakeePhone) System.loadLibrary("3djni");
        }
        catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load lib3djni.so");
            ule.printStackTrace();
        }
    }

    public synchronized static int EstarLcm3DOn() {
        if(GlobalVariable.isTakeePhone)
            return nativeestarScreen3dOn();
        else
            return 0;
    	}
	
    
    public synchronized static int EstarLcm3DOff() {
        if(GlobalVariable.isTakeePhone)
            return nativeestarScreen3dOff();
        else
            return 0;
    }


    private static native int nativeestarScreen3dOn();
    private static native int nativeestarScreen3dOff();    
}