package com.example.androidadvancedcourse.HomeFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.example.androidadvancedcourse.HomeFragment.adapters.sliderImageAdapter;
import com.example.androidadvancedcourse.MainActivity;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    MainActivity mainActivity;
    AppViewmodel appViewModel;

    @Inject
    @Named("myname")
    String name;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewmodel.class);

        setupViewPager2();


        // Inflate the layout for this fragment
        return fragmentHomeBinding.getRoot();
    }

    private void setupViewPager2() {
        appViewModel.getMutableLiveData().observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> pics) {
                fragmentHomeBinding.viewPagerImageSlider.setAdapter(new sliderImageAdapter(pics));
                fragmentHomeBinding.viewPagerImageSlider.setOffscreenPageLimit(3);
//                fragmentHomeBinding.viewPagerImageSlider.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupToolbar(View view){
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.homeFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("CryptoBs");
                }
            }
        });


    }
}