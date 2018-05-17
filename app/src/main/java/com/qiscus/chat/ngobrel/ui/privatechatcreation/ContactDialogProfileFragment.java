package com.qiscus.chat.ngobrel.ui.privatechatcreation;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiscus.chat.ngobrel.NgobrelApp;
import com.qiscus.chat.ngobrel.R;
import com.qiscus.chat.ngobrel.data.model.User;
import com.qiscus.chat.ngobrel.data.repository.ChatRoomRepository;
import com.qiscus.chat.ngobrel.ui.homepagetab.HomePageTabActivity;
import com.qiscus.chat.ngobrel.util.ChatRoomNavigator;
import com.qiscus.nirmana.Nirmana;

/**
 * Created by asyrof on 18/12/17.
 */
public class ContactDialogProfileFragment extends DialogFragment {
    private static final String USER_KEY = "USER_KEY";

    private User user;
    private ChatRoomRepository chatRoomRepository;

    public static ContactDialogProfileFragment newInstance(User user) {
        ContactDialogProfileFragment fragment = new ContactDialogProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER_KEY, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_profile, container, false);
        TextView contactName = rootView.findViewById(R.id.contact_display_name);
        TextView contactEmail = rootView.findViewById(R.id.contact_user_email);

        user = getArguments().getParcelable(USER_KEY);
        if (user == null) {
            throw new RuntimeException("Please provide a user");
        }

        chatRoomRepository = NgobrelApp.getInstance().getComponent().getChatRoomRepository();

        contactName.setText(user.getName());
        contactEmail.setText(user.getId());
        ImageView contactAvatar = rootView.findViewById(R.id.contact_picture);
        String avatarUrl = user.getAvatarUrl();
        Nirmana.getInstance().get().load(avatarUrl).centerCrop().into(contactAvatar);

        rootView.findViewById(R.id.startChat).setOnClickListener(v -> {
            chatRoomRepository.createChatRoom(user,
                    qiscusChatRoom -> {
                        ChatRoomNavigator.openChatRoom(getActivity(), qiscusChatRoom)
                                .withParentClass(HomePageTabActivity.class)
                                .start();
                        dismiss();
                    },
                    Throwable::printStackTrace);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }
}