//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

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
