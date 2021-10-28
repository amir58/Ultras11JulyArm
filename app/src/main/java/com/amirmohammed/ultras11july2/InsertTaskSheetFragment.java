package com.amirmohammed.ultras11july2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amirmohammed.ultras11july2.databinding.FragmentInsertTaskSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class InsertTaskSheetFragment extends BottomSheetDialogFragment {
    //    EditText editTextTitle, editTextDate, editTextTime;
//    MaterialButton materialButtonInsert;
    IInsertTask iInsertTask;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    FragmentInsertTaskSheetBinding binding;

    public InsertTaskSheetFragment(IInsertTask iInsertTask) {
        this.iInsertTask = iInsertTask;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insert_task_sheet, container, false);
//        return inflater.inflate(R.layout.fragment_insert_task_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        editTextTitle = view.findViewById(R.id.insert_et_title);
//        editTextDate = view.findViewById(R.id.insert_et_date);
//        editTextTime = view.findViewById(R.id.insert_et_time);
//        materialButtonInsert = view.findViewById(R.id.btn_insert_task);

        binding.btnInsertTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.insertEtTitle.getText().toString();
                String date = binding.insertEtDate.getText().toString();
                String time = binding.insertEtTime.getText().toString();

                String randomId = String.valueOf(System.currentTimeMillis());

                Task task = new Task(randomId, title, date, time);

//                TasksDatabase.getInstance(requireContext()).tasksDao().insertTask(task);

                firestore.collection("julyUsers").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("myTasks").document(randomId).set(task);


                dismiss();
            }
        });

    }


}