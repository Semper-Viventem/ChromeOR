package ru.semper_viventem.chromeor.presentation.view.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.pawegio.kandroid.find
import com.pawegio.kandroid.inflateLayout
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.model.LoginEntity

/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class LoginListAdapter(
        val mSelectLoginModelListener: SelectLoginModelListener
    ) : RecyclerView.Adapter<LoginListAdapter.LoginViwHolder>() {

    var mMainData: List<LoginEntity> = emptyList()
    var mData: List<LoginEntity> = emptyList()

    fun setData(loginEntityList: List<LoginEntity>) {
        mData = loginEntityList
        mMainData = loginEntityList
        notifyDataSetChanged()
    }

    fun setSearchQuery(query: String) {
        mData = mMainData.filter { (it.originUrl.contains(query) ||
                    it.actionUrl.contains(query) ||
                    it.passwordValue.contains(query)) ||
                    it.usernameValue.contains(query)}

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginViwHolder {
        return LoginViwHolder(parent.context.inflateLayout(R.layout.holder_login_list, attachToRoot = false),
                mSelectLoginModelListener, parent.context)
    }

    override fun onBindViewHolder(holder: LoginViwHolder, position: Int) {
        holder.bind(mData.get(position))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    /********************************* HOLDER ******************************************************/
    class LoginViwHolder(itemView: View, val selectListener: SelectLoginModelListener, val context: Context): RecyclerView.ViewHolder(itemView){

        fun bind(loginEntity: LoginEntity) {
            itemView.find<TextView>(R.id.vTextViewActionUrl).text = context.getString(R.string.holder_login_action, loginEntity.actionUrl)
            itemView.find<TextView>(R.id.vTextViewOriginUrl).text = context.getString(R.string.holder_login_list_origin, loginEntity.originUrl)
            itemView.find<TextView>(R.id.vTextViewUsername).text = context.getString(R.string.holder_login_username, loginEntity.usernameValue)
            itemView.find<TextView>(R.id.vTextViewPassword).text = context.getString(R.string.holder_login_password, loginEntity.passwordValue)

            itemView.find<ImageButton>(R.id.vImageButtonShare).setOnClickListener {
                selectListener.onShareButtonClicked(loginEntity)
            }
            itemView.find<LinearLayout>(R.id.vBodyLayout).setOnClickListener {
                selectListener.onLoginModelSelected(loginEntity)
            }
        }
    }

    interface SelectLoginModelListener {
        fun onLoginModelSelected(loginEntity: LoginEntity)
        fun onShareButtonClicked(loginEntity: LoginEntity)
    }
}