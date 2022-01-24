package com.emmanuelmuturia.mymoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends Fragment {
    //Declaring private member variables
    private RecyclerView dailyRecyclerView;
    private View view;
    private FirebaseFirestore db;
    private TextView txt;
    View rootView;
    private RelativeLayout relativeLayout;

    private DatabaseReference mExpDat;
    private FirebaseAuth mAuth;

    public DailyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_daily, container, false);

        mAuth = FirebaseAuth.getInstance();

        relativeLayout = rootView.findViewById(R.id.rel_layout);

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mExpDat = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());

        db = FirebaseFirestore.getInstance();

        //Initializing the recycler view
        dailyRecyclerView = rootView.findViewById(R.id.recycler_daily);
        //Set a layout for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        dailyRecyclerView.setHasFixedSize(true);
        dailyRecyclerView.setLayoutManager(linearLayoutManager);

        updateUI();

        return rootView;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }

    public void updateUI() {
        FirebaseRecyclerOptions<Expense> options = new FirebaseRecyclerOptions.Builder<Expense>()
                .setQuery(mExpDat, Expense.class)
                .build();

        FirebaseRecyclerAdapter<Expense, myViewHolder> adapter = new FirebaseRecyclerAdapter<Expense, myViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_list_view, null);

//                myViewHolder vh = new myViewHolder(view);
//                vh.setOnClickListener(new myViewHolder.ClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        System.out.println("He was clicked");
//                        Toast.makeText(getContext(), "Was clicked", Toast.LENGTH_SHORT);
//                    }
//                });

                return new myViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position, @NonNull @NotNull Expense model) {
                String resDate = model.getExpenseDate();
                SimpleDateFormat sd = new SimpleDateFormat("E, dd MMM yyyy");
                try {
                    Date date = sd.parse(resDate);
                    SimpleDateFormat sdi = new SimpleDateFormat("dd");
                    holder.setDay(sdi.format(date));
                    System.out.println("Current Day" + date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                try {
                    Date date = sd.parse(resDate);
                    SimpleDateFormat sdn = new SimpleDateFormat("E");
                    holder.setDailyName(sdn.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    Date date = sd.parse(resDate);
                    SimpleDateFormat sdn = new SimpleDateFormat("yyyy.MM");
                    holder.setDate(sdn.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


//                holder.setDate(model.getExpenseDate());
                holder.setExpenseTitle(model.getExpenseTitle());
                holder.setSpend("Ksh. " + model.getExpenseAmount());

            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN|ItemTouchHelper.UP, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();

                DatabaseReference reference = adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getRef();
                reference.removeValue();
                System.out.println("This is the removed Database Reference: "+reference);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

//                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "An Expense was removed", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        mExpDat.setValue(reference);
//
//                        adapter.notifyItemInserted(viewHolder.getAdapterPosition());
//                    }
//                }).show();
            }
        });
        helper.attachToRecyclerView(dailyRecyclerView);

        ItemTouchHelper helperEdit = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN|ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                Intent intent = new Intent(getContext(), EditDailyExpenseActivity.class);
                final String eID = adapter.getRef(viewHolder.getAdapterPosition()).getKey();
                System.out.println("The key is "+eID);
                intent.putExtra("expenseID", eID);
                startActivity(intent);

//                adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getRef().removeValue();
//
//
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        });
        helperEdit.attachToRecyclerView(dailyRecyclerView);

        dailyRecyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public CardView cardView;
        public TextView expDesc;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mview = itemView;
            cardView = itemView.findViewById(R.id.daily_card);
            expDesc = itemView.findViewById(R.id.expense_desc);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mCLickListener.onItemClick(v, getAdapterPosition());
//                }
//            });
        }

//        private myViewHolder.ClickListener mCLickListener;
//
//        public interface ClickListener{
//            public void onItemClick(View view, int position);
//        }
//
//        public void setOnClickListener(myViewHolder.ClickListener clickListener) {
//            mCLickListener = clickListener;
//        }

        public void deleteItem(int position) {

        }

        private void setExpenseTitle(String expTitle) {
            TextView mTitle = mview.findViewById(R.id.expense_desc);
            mTitle.setText(expTitle);
        }

        private void setSpend(String spend) {
            TextView mspend = mview.findViewById(R.id.spend);

//            String amount = String.valueOf(mspend);
            mspend.setText(spend);
        }

        private void setDate(String date) {
            TextView mdate = mview.findViewById(R.id.daily_date);
            mdate.setText(date);
        }

        private void setDay(String day) {
            TextView dailyDay = mview.findViewById(R.id.daily_day);
            dailyDay.setText(day);
        }

        private void setDailyName(String name) {
            TextView dailyName = mview.findViewById(R.id.daily_day_name);
            dailyName.setText(name);
        }
    }
}