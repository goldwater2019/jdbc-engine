/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.ane56.engine.jdbc.thrit.enumeration;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2022-04-02")
public enum TJDBCQueryStatus implements org.apache.thrift.TEnum {
  OK(0),
  FAILED(1);

  private final int value;

  private TJDBCQueryStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static TJDBCQueryStatus findByValue(int value) { 
    switch (value) {
      case 0:
        return OK;
      case 1:
        return FAILED;
      default:
        return null;
    }
  }
}
