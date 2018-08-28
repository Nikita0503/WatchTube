package com.example.watchtube;

/**
 * Created by Nikita on 28.05.2018.
 */

public interface Contract {
    public interface View {
        //public void showMessage(String result);
    }

    interface Presenter {
        public void onStart();
        public void onStop();
    }
}
