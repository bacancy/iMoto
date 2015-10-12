package common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiredave.imoto.EditProfileActivity;
import com.wiredave.imoto.LoginActivity;
import com.wiredave.imoto.R;


public class SampleListFragment extends ListFragment {

    Context con = null;
    SharedPreferences myPrefs;
    private String email;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.list, null);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SampleAdapter adapter = new SampleAdapter(getActivity());

        adapter.add(new SampleItem("Home",R.drawable.menu_icon_home));
        adapter.add(new SampleItem("Products", R.drawable.menu_icon_products));
        adapter.add(new SampleItem("Place Order", R.drawable.menu_icon_place_order));
        adapter.add(new SampleItem("Order History", R.drawable.menu_icon_order_history));
        adapter.add(new SampleItem("Account Information", R.drawable.menu_icon_acc_info));
        adapter.add(new SampleItem("Logout", R.drawable.menu_icon_logout));


        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        if (position == 0) {
			/*startActivity(new Intent(con, SignupActivity.class));
			getActivity().finish();*/

        } else if (position == 1) {


        } else if (position == 2) {


        } else if (position == 3) {




        }else if (position == 4) {

            startActivity(new Intent(con, EditProfileActivity.class));
            getActivity().finish();


        }else if (position == 5) {


            startActivity(new Intent(con, LoginActivity.class));
            getActivity().finish();


        }

        Toast.makeText(con, "position: " + position, Toast.LENGTH_SHORT).show();
    }

    private class SampleItem {
        public String tag;
        public int iconRes;

        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class SampleAdapter extends ArrayAdapter<SampleItem> {

        public SampleAdapter(Context context) {
            super(context, 0);
            con = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.row, null);
            }
			/*
			 * ImageView icon = (ImageView) convertView
			 * .findViewById(R.id.row_icon);
			 * icon.setImageResource(getItem(position).iconRes);
			 */
            TextView title = (TextView) convertView
                    .findViewById(R.id.row_title);
            title.setText(getItem(position).tag);

            return convertView;
        }
    }
}
