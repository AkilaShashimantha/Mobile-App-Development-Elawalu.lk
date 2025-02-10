package lk.payhere.androidsdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentInitResponse;

public class MethodAdapter extends RecyclerView.Adapter<MethodAdapter.MethoadViewHolder> {

    private final Context context;
    private final List<NewInitResponse.PaymentMethod> data;
    private OnPaymentMethodClick onPaymentMethodClick;


    public MethodAdapter(Context context, List<NewInitResponse.PaymentMethod> data){
        this.context = context;
        this.data = data;
    }

    public void setOnPaymentMethodClick(OnPaymentMethodClick onPaymentMethodClick){
        this.onPaymentMethodClick = onPaymentMethodClick;
    }

    @NonNull
    @Override
    public MethoadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_payment_method_item,parent,false);
        return new MethoadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MethodAdapter.MethoadViewHolder holder, int position) {

        // final PaymentMethodResponse.Data object = this.data.get(position);
        final NewInitResponse.PaymentMethod object = this.data.get(position);
        if(object != null) {
            //mapMethodIcons(object.getMethod(), holder.imageView);
            Glide.with(context).load(object.getView().getImageUrl()).into(holder.imageView);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPaymentMethodClick != null)
                    onPaymentMethodClick.onclick(object);
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MethoadViewHolder extends RecyclerView.ViewHolder{

        public ConstraintLayout parent;
        public ImageView imageView;

        public MethoadViewHolder(View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.item_parent);
            imageView = itemView.findViewById(R.id.method_icon_image);
        }
    }

    public interface OnPaymentMethodClick{
        void onclick(NewInitResponse.PaymentMethod paymentMethod);
    }

    private void mapMethodIcons(String method,ImageView iv){
        switch (method){
            case "VISA" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.visa));
                break;
            case "MASTER" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.master));
                break;
            case "AMEX" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.amex));
                break;
            case "FRIMI" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.frimi));
                break;
            case "LOLC_IPAY" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.methoad_background));
                break;
            case "QPLUS" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.q_pay));
                break;
            case "VISHWA" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.vishwa));
                break;
            case "MCASH" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.mcash));
                break;
            case "EZCASH" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ezcash));
                break;
            case "GENIE" :
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.genie));
                break;
        }
    }

}

