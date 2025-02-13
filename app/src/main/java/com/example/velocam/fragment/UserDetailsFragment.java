package com.example.velocam.fragment;


import com.example.velocam.R;
import com.example.velocam.utils.Config;
import com.squareup.picasso.Picasso;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserDetailsFragment extends Fragment {
	ImageView imgUser;
	TextView tvName, tvEmail;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_user_details, container,false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("User Details");
		bindView();

//		String name =  getArguments().getString(Config.KEY_NAME);
//		String email = getArguments().getString(Config.KEY_EMAIL);
//		String image=getArguments().getString(Config.KEY_IMAGE);
//
//		tvEmail.setText(email);
//		tvName.setText(name);
//		Picasso.with(getActivity()).load(Config.URL_IMAGES+image).into(imgUser);
	}

	private void bindView() {
		imgUser = (ImageView) getActivity().findViewById(R.id.imgUserDetails);
		tvName = (TextView) getActivity().findViewById(R.id.tvUnmUserDetail);
		tvEmail = (TextView) getActivity().findViewById(
				R.id.tvUserEmailUserDetails);
	}

}
