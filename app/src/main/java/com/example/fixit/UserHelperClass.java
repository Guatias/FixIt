package com.example.fixit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class UserHelperClass implements Parcelable {

    String nome;
    String sobrenome;
    String email;
    String celular;
    String data_nasc;
    String tipo_conta;
    String cpf;

    public UserHelperClass() {
    }

    protected UserHelperClass(Parcel in) {
        nome = in.readString();
        sobrenome = in.readString();
        email = in.readString();
        celular = in.readString();
        data_nasc = in.readString();
        tipo_conta = in.readString();
        cpf = in.readString();
    }

    public static final Creator<UserHelperClass> CREATOR = new Creator<UserHelperClass>() {
        @Override
        public UserHelperClass createFromParcel(Parcel in) {
            return new UserHelperClass(in);
        }

        @Override
        public UserHelperClass[] newArray(int size) {
            return new UserHelperClass[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getTipo_conta() {
        return tipo_conta;
    }

    public void setTipo_conta(String tipo_conta) {
        this.tipo_conta = tipo_conta;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(email);
        dest.writeString(celular);
        dest.writeString(data_nasc);
        dest.writeString(tipo_conta);
        dest.writeString(cpf);
    }

    public void retrieveUserData(Map<String, Object> users, String email) {
        try {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                if (singleUser.get("email").toString().equals(email)){
                    this.celular = singleUser.get("celular").toString();
                    this.data_nasc = singleUser.get("data_nasc").toString();
                    this.email = singleUser.get("email").toString();
                    this.nome = singleUser.get("nome").toString();
                    this.sobrenome = singleUser.get("sobrenome").toString();
                    this.tipo_conta = singleUser.get("tipo_conta").toString();
                    this.cpf = singleUser.get("cpf").toString();
                }
            }
        } catch (Exception ex){

        }
    }
}
