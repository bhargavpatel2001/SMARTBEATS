package ca.shalominc.it.smartbeats;

public class ModeItem {
    private String mModeName;
    private int mModeImage;

    public ModeItem(String modeName, int modeImage){
        mModeName=modeName;
        mModeImage=modeImage;
    }

    public String getModeName(){
        return mModeName;
    }

    public int getModeImage(){
        return mModeImage;
    }
}
