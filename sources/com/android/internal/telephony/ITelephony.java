package com.android.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.NeighboringCellInfo;
import java.util.List;

public interface ITelephony extends IInterface {
    void answerRingingCall() throws RemoteException;

    void call(String str) throws RemoteException;

    void cancelMissedCallsNotification() throws RemoteException;

    void dial(String str) throws RemoteException;

    int disableApnType(String str) throws RemoteException;

    boolean disableDataConnectivity() throws RemoteException;

    void disableLocationUpdates() throws RemoteException;

    int enableApnType(String str) throws RemoteException;

    boolean enableDataConnectivity() throws RemoteException;

    void enableLocationUpdates() throws RemoteException;

    boolean endCall() throws RemoteException;

    String getActiveGateway(String str, String str2) throws RemoteException;

    String getActiveInterfaceName(String str, String str2) throws RemoteException;

    String getActiveIpAddress(String str, String str2) throws RemoteException;

    int getActivePhoneType() throws RemoteException;

    int getCallState() throws RemoteException;

    int getCdmaEriIconIndex() throws RemoteException;

    int getCdmaEriIconMode() throws RemoteException;

    String getCdmaEriText() throws RemoteException;

    boolean getCdmaNeedsProvisioning() throws RemoteException;

    Bundle getCellLocation() throws RemoteException;

    int getDataActivity() throws RemoteException;

    int getDataState() throws RemoteException;

    int getIccPin1RetryCount() throws RemoteException;

    List<NeighboringCellInfo> getNeighboringCellInfo() throws RemoteException;

    int getNetworkType() throws RemoteException;

    int getVoiceMessageCount() throws RemoteException;

    boolean handlePinMmi(String str) throws RemoteException;

    boolean hasIccCard() throws RemoteException;

    boolean isDataConnectivityPossible() throws RemoteException;

    boolean isIdle() throws RemoteException;

    boolean isModemPowerSave() throws RemoteException;

    boolean isOffhook() throws RemoteException;

    boolean isRadioOn() throws RemoteException;

    boolean isRinging() throws RemoteException;

    boolean isSimPinEnabled() throws RemoteException;

    int sendOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean setRadio(boolean z) throws RemoteException;

    boolean showCallScreen() throws RemoteException;

    boolean showCallScreenWithDialpad(boolean z) throws RemoteException;

    void silenceRinger() throws RemoteException;

    boolean supplyPin(String str) throws RemoteException;

    void toggleRadioOnOff() throws RemoteException;

    void updateServiceLocation() throws RemoteException;

    public static abstract class Stub extends Binder implements ITelephony {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
        static final int TRANSACTION_answerRingingCall = 6;
        static final int TRANSACTION_call = 2;
        static final int TRANSACTION_cancelMissedCallsNotification = 13;
        static final int TRANSACTION_dial = 1;
        static final int TRANSACTION_disableApnType = 22;
        static final int TRANSACTION_disableDataConnectivity = 24;
        static final int TRANSACTION_disableLocationUpdates = 20;
        static final int TRANSACTION_enableApnType = 21;
        static final int TRANSACTION_enableDataConnectivity = 23;
        static final int TRANSACTION_enableLocationUpdates = 19;
        static final int TRANSACTION_endCall = 5;
        static final int TRANSACTION_getActiveGateway = 43;
        static final int TRANSACTION_getActiveInterfaceName = 41;
        static final int TRANSACTION_getActiveIpAddress = 42;
        static final int TRANSACTION_getActivePhoneType = 31;
        static final int TRANSACTION_getCallState = 28;
        static final int TRANSACTION_getCdmaEriIconIndex = 33;
        static final int TRANSACTION_getCdmaEriIconMode = 34;
        static final int TRANSACTION_getCdmaEriText = 35;
        static final int TRANSACTION_getCdmaNeedsProvisioning = 36;
        static final int TRANSACTION_getCellLocation = 26;
        static final int TRANSACTION_getDataActivity = 29;
        static final int TRANSACTION_getDataState = 30;
        static final int TRANSACTION_getIccPin1RetryCount = 44;
        static final int TRANSACTION_getNeighboringCellInfo = 27;
        static final int TRANSACTION_getNetworkType = 38;
        static final int TRANSACTION_getVoiceMessageCount = 37;
        static final int TRANSACTION_handlePinMmi = 15;
        static final int TRANSACTION_hasIccCard = 40;
        static final int TRANSACTION_isDataConnectivityPossible = 25;
        static final int TRANSACTION_isIdle = 10;
        static final int TRANSACTION_isModemPowerSave = 39;
        static final int TRANSACTION_isOffhook = 8;
        static final int TRANSACTION_isRadioOn = 11;
        static final int TRANSACTION_isRinging = 9;
        static final int TRANSACTION_isSimPinEnabled = 12;
        static final int TRANSACTION_sendOemRilRequestRaw = 32;
        static final int TRANSACTION_setRadio = 17;
        static final int TRANSACTION_showCallScreen = 3;
        static final int TRANSACTION_showCallScreenWithDialpad = 4;
        static final int TRANSACTION_silenceRinger = 7;
        static final int TRANSACTION_supplyPin = 14;
        static final int TRANSACTION_toggleRadioOnOff = 16;
        static final int TRANSACTION_updateServiceLocation = 18;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelephony)) {
                return new Proxy(obj);
            }
            return (ITelephony) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i;
            int i2;
            int i3;
            byte[] _arg1;
            int i4;
            int i5;
            int i6;
            boolean _arg0;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int i13;
            int i14;
            int i15;
            boolean _arg02;
            int i16;
            int i17;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    dial(data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    call(data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result = showCallScreen();
                    reply.writeNoException();
                    if (_result) {
                        i17 = 1;
                    } else {
                        i17 = 0;
                    }
                    reply.writeInt(i17);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    boolean _result2 = showCallScreenWithDialpad(_arg02);
                    reply.writeNoException();
                    if (_result2) {
                        i16 = 1;
                    } else {
                        i16 = 0;
                    }
                    reply.writeInt(i16);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = endCall();
                    reply.writeNoException();
                    if (_result3) {
                        i15 = 1;
                    } else {
                        i15 = 0;
                    }
                    reply.writeInt(i15);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    answerRingingCall();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    silenceRinger();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result4 = isOffhook();
                    reply.writeNoException();
                    if (_result4) {
                        i14 = 1;
                    } else {
                        i14 = 0;
                    }
                    reply.writeInt(i14);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result5 = isRinging();
                    reply.writeNoException();
                    if (_result5) {
                        i13 = 1;
                    } else {
                        i13 = 0;
                    }
                    reply.writeInt(i13);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result6 = isIdle();
                    reply.writeNoException();
                    if (_result6) {
                        i12 = 1;
                    } else {
                        i12 = 0;
                    }
                    reply.writeInt(i12);
                    return true;
                case TRANSACTION_isRadioOn /*11*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result7 = isRadioOn();
                    reply.writeNoException();
                    if (_result7) {
                        i11 = 1;
                    } else {
                        i11 = 0;
                    }
                    reply.writeInt(i11);
                    return true;
                case TRANSACTION_isSimPinEnabled /*12*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result8 = isSimPinEnabled();
                    reply.writeNoException();
                    if (_result8) {
                        i10 = 1;
                    } else {
                        i10 = 0;
                    }
                    reply.writeInt(i10);
                    return true;
                case TRANSACTION_cancelMissedCallsNotification /*13*/:
                    data.enforceInterface(DESCRIPTOR);
                    cancelMissedCallsNotification();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_supplyPin /*14*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result9 = supplyPin(data.readString());
                    reply.writeNoException();
                    if (_result9) {
                        i9 = 1;
                    } else {
                        i9 = 0;
                    }
                    reply.writeInt(i9);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result10 = handlePinMmi(data.readString());
                    reply.writeNoException();
                    if (_result10) {
                        i8 = 1;
                    } else {
                        i8 = 0;
                    }
                    reply.writeInt(i8);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRadioOnOff();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setRadio /*17*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    boolean _result11 = setRadio(_arg0);
                    reply.writeNoException();
                    if (_result11) {
                        i7 = 1;
                    } else {
                        i7 = 0;
                    }
                    reply.writeInt(i7);
                    return true;
                case TRANSACTION_updateServiceLocation /*18*/:
                    data.enforceInterface(DESCRIPTOR);
                    updateServiceLocation();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_enableLocationUpdates /*19*/:
                    data.enforceInterface(DESCRIPTOR);
                    enableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    disableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_enableApnType /*21*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = enableApnType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case TRANSACTION_disableApnType /*22*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result13 = disableApnType(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case TRANSACTION_enableDataConnectivity /*23*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result14 = enableDataConnectivity();
                    reply.writeNoException();
                    if (_result14) {
                        i6 = 1;
                    } else {
                        i6 = 0;
                    }
                    reply.writeInt(i6);
                    return true;
                case TRANSACTION_disableDataConnectivity /*24*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result15 = disableDataConnectivity();
                    reply.writeNoException();
                    if (_result15) {
                        i5 = 1;
                    } else {
                        i5 = 0;
                    }
                    reply.writeInt(i5);
                    return true;
                case TRANSACTION_isDataConnectivityPossible /*25*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result16 = isDataConnectivityPossible();
                    reply.writeNoException();
                    if (_result16) {
                        i4 = 1;
                    } else {
                        i4 = 0;
                    }
                    reply.writeInt(i4);
                    return true;
                case TRANSACTION_getCellLocation /*26*/:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _result17 = getCellLocation();
                    reply.writeNoException();
                    if (_result17 != null) {
                        reply.writeInt(1);
                        _result17.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case TRANSACTION_getNeighboringCellInfo /*27*/:
                    data.enforceInterface(DESCRIPTOR);
                    List<NeighboringCellInfo> _result18 = getNeighboringCellInfo();
                    reply.writeNoException();
                    reply.writeTypedList(_result18);
                    return true;
                case TRANSACTION_getCallState /*28*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result19 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case TRANSACTION_getDataActivity /*29*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result20 = getDataActivity();
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case TRANSACTION_getDataState /*30*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result21 = getDataState();
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case TRANSACTION_getActivePhoneType /*31*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result22 = getActivePhoneType();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg03 = data.createByteArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    int _result23 = sendOemRilRequestRaw(_arg03, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    reply.writeByteArray(_arg1);
                    return true;
                case TRANSACTION_getCdmaEriIconIndex /*33*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result24 = getCdmaEriIconIndex();
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case TRANSACTION_getCdmaEriIconMode /*34*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result25 = getCdmaEriIconMode();
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case TRANSACTION_getCdmaEriText /*35*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result26 = getCdmaEriText();
                    reply.writeNoException();
                    reply.writeString(_result26);
                    return true;
                case TRANSACTION_getCdmaNeedsProvisioning /*36*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result27 = getCdmaNeedsProvisioning();
                    reply.writeNoException();
                    if (_result27) {
                        i3 = 1;
                    } else {
                        i3 = 0;
                    }
                    reply.writeInt(i3);
                    return true;
                case TRANSACTION_getVoiceMessageCount /*37*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result28 = getVoiceMessageCount();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case TRANSACTION_getNetworkType /*38*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result29 = getNetworkType();
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case TRANSACTION_isModemPowerSave /*39*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result30 = isModemPowerSave();
                    reply.writeNoException();
                    if (_result30) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    reply.writeInt(i2);
                    return true;
                case TRANSACTION_hasIccCard /*40*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result31 = hasIccCard();
                    reply.writeNoException();
                    if (_result31) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_getActiveInterfaceName /*41*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result32 = getActiveInterfaceName(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result32);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    String _result33 = getActiveIpAddress(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result33);
                    return true;
                case TRANSACTION_getActiveGateway /*43*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result34 = getActiveGateway(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result34);
                    return true;
                case TRANSACTION_getIccPin1RetryCount /*44*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result35 = getIccPin1RetryCount();
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ITelephony {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void dial(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void call(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean showCallScreen() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean showCallScreenWithDialpad(boolean showDialpad) throws RemoteException {
                int i;
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (showDialpad) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean endCall() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void answerRingingCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void silenceRinger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOffhook() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRinging() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIdle() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRadioOn() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isRadioOn, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSimPinEnabled() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isSimPinEnabled, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelMissedCallsNotification() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_cancelMissedCallsNotification, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPin(String pin) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    this.mRemote.transact(Stub.TRANSACTION_supplyPin, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmi(String dialString) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleRadioOnOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRadio(boolean turnOn) throws RemoteException {
                int i;
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (turnOn) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setRadio, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateServiceLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_updateServiceLocation, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_enableLocationUpdates, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableApnType(String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    this.mRemote.transact(Stub.TRANSACTION_enableApnType, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int disableApnType(String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    this.mRemote.transact(Stub.TRANSACTION_disableApnType, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableDataConnectivity() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_enableDataConnectivity, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableDataConnectivity() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_disableDataConnectivity, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataConnectivityPossible() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isDataConnectivityPossible, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getCellLocation() throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCellLocation, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<NeighboringCellInfo> getNeighboringCellInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getNeighboringCellInfo, _data, _reply, 0);
                    _reply.readException();
                    return _reply.createTypedArrayList(NeighboringCellInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCallState, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getDataActivity, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getDataState, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActivePhoneType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getActivePhoneType, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendOemRilRequestRaw(byte[] request, byte[] response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(request);
                    if (response == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(response.length);
                    }
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(response);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconIndex() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCdmaEriIconIndex, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCdmaEriIconMode, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaEriText() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCdmaEriText, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCdmaNeedsProvisioning() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCdmaNeedsProvisioning, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoiceMessageCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getVoiceMessageCount, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNetworkType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getNetworkType, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isModemPowerSave() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isModemPowerSave, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasIccCard() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_hasIccCard, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    } else {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getActiveInterfaceName(String apnType, String ipv) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(ipv);
                    this.mRemote.transact(Stub.TRANSACTION_getActiveInterfaceName, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getActiveIpAddress(String apnType, String ipv) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(ipv);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getActiveGateway(String apnType, String ipv) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(ipv);
                    this.mRemote.transact(Stub.TRANSACTION_getActiveGateway, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getIccPin1RetryCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getIccPin1RetryCount, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
