package com.example.expviewer

import android.content.Context
import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GetDataServer (val context: Context) {
    private var expDB: ExpDB
    init {
        expDB = ExpDB(context)
    }

    fun getData(): Observable<Boolean> {
        return Observable.create(object: ObservableOnSubscribe<Boolean>{
            override fun subscribe(emitter: ObservableEmitter<Boolean>) {
                Log.d("subscribe", "OK")
                RetrofitClientInstance.getInstance()
                    .getAllExp()
                    .subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object: Observer<Response<List<ExpData>>>{
                        override fun onComplete() {}

                        override fun onSubscribe(d: Disposable) {}

                        override fun onNext(t: Response<List<ExpData>>) {
                            if (t.code() == 200) {
                                Log.d("onNext", "Ответ получен")
                                val body = t.body()
                                expDB.addAllExp(body!!)
                            } else {
                                closeDB()
                                emitter.onError(OurException(t.code()))
                            }
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                    })
            }

        })

    }

    private fun closeDB() {
        expDB.closeDB()
    }
    private fun clearBD() {
        expDB.clearTableDB()
    }
}