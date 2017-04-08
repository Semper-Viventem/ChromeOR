package ru.semper_viventem.chromeor.presentation.view.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.pawegio.kandroid.find
import com.pawegio.kandroid.inflateLayout
import kotlinx.android.synthetic.main.holder_login_list.view.*
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.presentation.model.LoginEntity

/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class LoginListAdapter(
        private val mListener: SelectLoginModelListener
    ) : RecyclerView.Adapter<LoginListAdapter.LoginViwHolder>() {

    var mData: List<LoginEntity> = emptyList()

    /**
     * Загрузить данные в список
     *
     * @param loginEntityList список [List] моделей данных авторизации [LoginEntity]
     */
    fun setData(loginEntityList: List<LoginEntity>) {
        mData = loginEntityList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginViwHolder {
        return LoginViwHolder(parent.context.inflateLayout(R.layout.holder_login_list, attachToRoot = false))
    }

    override fun onBindViewHolder(holder: LoginViwHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    /**
     * View Holder для списка
     */
    inner class LoginViwHolder(
            itemView: View
    ): RecyclerView.ViewHolder(itemView){

        fun bind(loginEntity: LoginEntity) {
            itemView.vTextViewActionUrl.text = itemView.context.getString(R.string.holder_login_action, loginEntity.actionUrl)
            itemView.vTextViewOriginUrl.text = itemView.context.getString(R.string.holder_login_list_origin, loginEntity.originUrl)
            itemView.vTextViewUsername.text = itemView.context.getString(R.string.holder_login_username, loginEntity.usernameValue)
            itemView.vTextViewPassword.text = itemView.context.getString(R.string.holder_login_password, loginEntity.passwordValue)

            itemView.find<ImageButton>(R.id.vImageButtonShare).setOnClickListener {
                mListener.onShareButtonClicked(loginEntity)
            }
            itemView.find<LinearLayout>(R.id.vBodyLayout).setOnClickListener {
                mListener.onItemSelected(loginEntity)
            }
        }
    }

    /**
     * Слушатели для списка
     */
    interface SelectLoginModelListener {

        /**
         * Нажатие на элемент списка
         *
         * @param loginEntity модель данных авторизации
         */
        fun onItemSelected(loginEntity: LoginEntity)

        /**
         * Нажатие на кнопку "поделиться"
         *
         * @param loginEntity модель данных авторизации
         */
        fun onShareButtonClicked(loginEntity: LoginEntity)
    }
}