package com.app.sample.mailbox;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.mailbox.data.Constant;
import com.app.sample.mailbox.data.GlobalVariable;
import com.app.sample.mailbox.data.Tools;
import com.app.sample.mailbox.model.Mail;
import com.app.sample.mailbox.model.People;
import com.app.sample.mailbox.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityComposeMail extends AppCompatActivity {
    public static final String EXTRA_OBJCT = "com.app.sample.mailbox.OBJ";

    private Toolbar toolbar;
    private ActionBar actionBar;
    private String address_receiver;
    private View parent_view;
    private AutoCompleteTextView address_to;
    private EditText subject, content;
    private GlobalVariable global;
    private People receiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_mail);
        parent_view = findViewById(android.R.id.content);
        global = (GlobalVariable) getApplication();
        initToolbar();

        address_to  = (AutoCompleteTextView) findViewById(R.id.address_to);
        subject     = (EditText) findViewById(R.id.subject);
        content     = (EditText) findViewById(R.id.content);

        receiver = (People) getIntent().getSerializableExtra(EXTRA_OBJCT);
        if(receiver != null){
            address_to.setText(receiver.getName());
            address_receiver = receiver.getAddress();
        }

        List<People> items = Constant.getPeopleData(this);
        final CustomerAdapter adapter = new CustomerAdapter(this, R.layout.row_people, items);
        address_to.setAdapter(adapter);
        //return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        address_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                address_receiver = adapter.getItem(i).getAddress();
                receiver = adapter.getItem(i);
                hideKeyboard();
            }
        });
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Compose");
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_compose_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_attch) {
            Snackbar.make(parent_view, "Attachment Clicked", Snackbar.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_send) {
            hideKeyboard();
            if(address_receiver==null){
                address_receiver = address_to.getText().toString();
                receiver = new People(global.getPeoples().size(), null, address_receiver, R.drawable.unknown_avatar);
            }
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(address_receiver).matches()){
                Snackbar.make(parent_view, "Invalid Email", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            if(subject.getText().toString().trim().equals("")){
                Snackbar.make(parent_view, "Subject Cannot Empty", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            if(content.getText().toString().trim().equals("")){
                Snackbar.make(parent_view, "Content Cannot Empty", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            People me = new People(0, this.getString(R.string.me_name), this.getString(R.string.me_mail), R.drawable.profile);


            global.addOutbox(
                    new Mail(
                            Long.parseLong("11" + global.getOutbox().size()),
                            "now",
                            me,
                            receiver,
                            subject.getText().toString(),
                            content.getText().toString()
                    ));
            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomerAdapter extends ArrayAdapter<People> {
        private List<People> items;
        private List<People> itemsAll;
        private List<People> suggestions;
        private int viewResourceId;
        private Context ctx;

        public CustomerAdapter(Context context, int viewResourceId, List<People> items) {
            super(context, viewResourceId, items);
            this.items = items;
            this.itemsAll = new ArrayList<>(items.size());
            this.itemsAll.addAll(items);
            this.suggestions = new ArrayList<>();
            this.viewResourceId = viewResourceId;
            this.ctx = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            People customer = items.get(position);
            if (customer != null) {
                TextView name = (TextView) v.findViewById(R.id.name);
                ImageView image = (ImageView) v.findViewById(R.id.image);
                TextView address = (TextView) v.findViewById(R.id.address);
                name.setText(customer.getName());
                address.setText(customer.getAddress());
                image.setVisibility(View.GONE);
                //Picasso.with(ctx).load(customer.getPhoto()).resize(100, 100).transform(new CircleTransform()).into(image);
            }
            return v;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                String str = ((People)(resultValue)).getName();
                return str;
            }
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                address_receiver = null;
                receiver = null;
                String query = constraint.toString().toLowerCase();
                if(constraint != null) {
                    suggestions.clear();
                    for (People customer : itemsAll) {
                        String name = customer.getName().toLowerCase();
                        String address = customer.getAddress().toLowerCase();
                        if(name.contains(query) || address.contains(query)){
                            suggestions.add(customer);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<People> filteredList = (ArrayList<People>) results.values;
                if(results != null && results.count > 0) {
                    clear();
                    for (People c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }
            }
        };

    }
}
