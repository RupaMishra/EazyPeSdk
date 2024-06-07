package com.example.easypepg.modal;

public class ResponseData {
     private String RESPONSE_DATE_TIME;
     private String RESPONSE_CODE;
     private String PG_TXN_MESSAGE;
     private String STATUS;
     private String PAY_ID;
     private String PG_REF_NUM;
     private String TXN_ID;
     private String ORDER_ID;
     private String RESPONSE_MESSAGE;
     private String TXNTYPE;
     private String HASH;

     public String getRESPONSE_DATE_TIME() {
          return RESPONSE_DATE_TIME;
     }

     public void setRESPONSE_DATE_TIME(String RESPONSE_DATE_TIME) {
          this.RESPONSE_DATE_TIME = RESPONSE_DATE_TIME;
     }

     public String getRESPONSE_CODE() {
          return RESPONSE_CODE;
     }

     public void setRESPONSE_CODE(String RESPONSE_CODE) {
          this.RESPONSE_CODE = RESPONSE_CODE;
     }

     public String getPG_TXN_MESSAGE() {
          return PG_TXN_MESSAGE;
     }

     public void setPG_TXN_MESSAGE(String PG_TXN_MESSAGE) {
          this.PG_TXN_MESSAGE = PG_TXN_MESSAGE;
     }

     public String getSTATUS() {
          return STATUS;
     }

     public void setSTATUS(String STATUS) {
          this.STATUS = STATUS;
     }

     public String getPAY_ID() {
          return PAY_ID;
     }

     public void setPAY_ID(String PAY_ID) {
          this.PAY_ID = PAY_ID;
     }

     public String getPG_REF_NUM() {
          return PG_REF_NUM;
     }

     public void setPG_REF_NUM(String PG_REF_NUM) {
          this.PG_REF_NUM = PG_REF_NUM;
     }

     public String getTXN_ID() {
          return TXN_ID;
     }

     public void setTXN_ID(String TXN_ID) {
          this.TXN_ID = TXN_ID;
     }

     public String getORDER_ID() {
          return ORDER_ID;
     }

     public void setORDER_ID(String ORDER_ID) {
          this.ORDER_ID = ORDER_ID;
     }

     public String getRESPONSE_MESSAGE() {
          return RESPONSE_MESSAGE;
     }

     public void setRESPONSE_MESSAGE(String RESPONSE_MESSAGE) {
          this.RESPONSE_MESSAGE = RESPONSE_MESSAGE;
     }

     public String getTXNTYPE() {
          return TXNTYPE;
     }

     public void setTXNTYPE(String TXNTYPE) {
          this.TXNTYPE = TXNTYPE;
     }

     public String getHASH() {
          return HASH;
     }

     public void setHASH(String HASH) {
          this.HASH = HASH;
     }

     @Override
     public String toString() {
          return "ResponseData{" +
                  "RESPONSE_DATE_TIME='" + RESPONSE_DATE_TIME + '\'' +
                  ", RESPONSE_CODE='" + RESPONSE_CODE + '\'' +
                  ", PG_TXN_MESSAGE='" + PG_TXN_MESSAGE + '\'' +
                  ", STATUS='" + STATUS + '\'' +
                  ", PAY_ID='" + PAY_ID + '\'' +
                  ", PG_REF_NUM='" + PG_REF_NUM + '\'' +
                  ", TXN_ID='" + TXN_ID + '\'' +
                  ", ORDER_ID='" + ORDER_ID + '\'' +
                  ", RESPONSE_MESSAGE='" + RESPONSE_MESSAGE + '\'' +
                  ", TXNTYPE='" + TXNTYPE + '\'' +
                  ", HASH='" + HASH + '\'' +
                  '}';
     }
}
