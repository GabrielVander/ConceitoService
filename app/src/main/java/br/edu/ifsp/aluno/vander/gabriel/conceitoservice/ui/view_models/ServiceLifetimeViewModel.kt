package br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServiceLifetimeViewModel : ViewModel() {
    private val _lifetime = MutableLiveData(0)
    val lifetime: LiveData<Int> = _lifetime

    fun onLifetimeValueChanged(newValue: Int) {
        _lifetime.postValue(newValue)
    }

}