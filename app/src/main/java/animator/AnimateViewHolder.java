package animator;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by milind.naik on 1/15/2016.
 */

public abstract class AnimateViewHolder extends RecyclerView.ViewHolder {

    public AnimateViewHolder(View itemView) {
        super(itemView);
    }

    public void preAnimateAddImpl() {
    }

    public void preAnimateRemoveImpl() {
    }

    public abstract void animateAddImpl(ViewPropertyAnimatorListener listener);

    public abstract void animateRemoveImpl(ViewPropertyAnimatorListener listener);
}
