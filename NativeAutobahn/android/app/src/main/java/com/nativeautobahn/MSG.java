package com.nativeautobahn;

public class MSG implements java.io.Serializable{
    public int num=0;
    public String content=null;
    public MSG(){

    }
    public int getNum(){return num;}
    public String getContent(){
        return content;
    }
    public void setNum(int num){
        this.num=num;
    }
    public void setContent(String content){
        this.content=content;
    }
}
