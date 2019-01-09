package com.geniusscan;

public class PromiseResult {
  public boolean isError = false;
  public String code;
  public String message;

  public String result = null;

  public static PromiseResult resolve() {
    return new PromiseResult();
  }
  public static PromiseResult resolve(String _result) {
    PromiseResult result = new PromiseResult();
    result.result = _result;
    return result;
  }
  public static PromiseResult reject(String errorCode, String errorMessage) {
    PromiseResult result = new PromiseResult();
    result.isError = true;
    result.code = errorCode;
    result.message = errorMessage;

    return result;
  }
}