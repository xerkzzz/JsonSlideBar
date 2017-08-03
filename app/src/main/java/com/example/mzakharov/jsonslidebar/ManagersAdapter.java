package com.example.mzakharov.jsonslidebar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xerks on 02.08.2017.
 */

public class ManagersAdapter extends ArrayAdapter<Managers> {


    ArrayList<Managers> ArrayListManagers;
    int Resourse;
    Context context;
    LayoutInflater vi;

    public ManagersAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Managers> objects) {
        super(context, resource, objects);
        ArrayListManagers = objects;
        Resourse = resource;
        this.context = context;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            convertView = vi.inflate(Resourse, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.firstname = (TextView) convertView.findViewById(R.id.firstname);
            holder.surname = (TextView) convertView.findViewById(R.id.surname);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        new DownloadImageTask(holder.avatar).execute(ArrayListManagers.get(position).getAvatar());
        holder.firstname.setText(ArrayListManagers.get(position).getFirstname());
        holder.surname.setText(ArrayListManagers.get(position).getSurname());
        holder.id.setText(ArrayListManagers.get(position).getId());

        return convertView;
    }

    static class ViewHolder {

        public ImageView avatar;
        public TextView id;
        public TextView firstname;
        public TextView surname;

    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView bmImage;
        public  DownloadImageTask(ImageView bmImage){
            this.bmImage=bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            //URL imgUrl = (URL);
            String urldisplay="http://dev.tabasko.ga/admin/images/595cbd7bd8f98.jpg";
            Bitmap mIcon= null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon= BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
            return  mIcon;
        }
        protected  void onPostExecute(Bitmap result){

            bmImage.setImageBitmap(result);

        }
    }
}
