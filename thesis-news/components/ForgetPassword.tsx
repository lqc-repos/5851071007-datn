import { Button, TextField } from "@mui/material";
import { useState } from "react";
import { useForm, Controller } from "react-hook-form";

const ForgetPassword: React.FC<{
  handleChangeNewPassword: (e: string) => void;
  setOpenNoti: (e: boolean) => void;
  setMessage: (e: string) => void;
}> = ({ handleChangeNewPassword, setMessage, setOpenNoti }) => {
  const { handleSubmit, control } = useForm<{ email: string; otp: string }>({
    defaultValues: {
      email: "",
      otp: "",
    },
  });

  const [isSendOtp, setIsSendOtp] = useState(false);

  const sendOtp = async (data: any) => {
    const resp = await fetch("http://localhost:8080/user/send-otp", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email: data.email }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp?.statusCode !== 200) {
      setOpenNoti(true);
      setMessage(resp?.message || resp?.data?.message);
      return;
    }

    if (resp?.statusCode === 200) {
      setOpenNoti(true);
      setMessage(resp?.message || resp?.data?.message);
      return true;
    }
  };

  const onSubmit = handleSubmit(async (data) => {
    if (!isSendOtp) {
      const respSendOtp = await sendOtp(data);
      if (respSendOtp) {
        setIsSendOtp(true);
      }
      return;
    }
    // Verify Otp
    const resp = await fetch("http://localhost:8080/user/verify-otp", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email: data.email, otp: data.otp }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp.statusCode !== 200) {
      setOpenNoti(true);
      setMessage(resp.message || resp?.data?.message);
    }
    if (resp.statusCode === 200) {
      setOpenNoti(true);
      setMessage(resp.message || resp?.data?.message);
      handleChangeNewPassword(data.email);
      return;
    }
  });

  const handleReSendOTP = handleSubmit((data) => {
    sendOtp(data);
  });

  return (
    <>
      <div>
        <Controller
          name="email"
          control={control}
          render={({ field }) => (
            <TextField
              label="Email"
              placeholder="Vui lòng nhập email"
              variant="outlined"
              className="w-full pb-3"
              autoComplete="no"
              {...field}
            />
          )}
        />
        {isSendOtp && (
          <Controller
            name="otp"
            control={control}
            render={({ field }) => (
              <TextField
                label="Otp"
                placeholder="Vui lòng nhập otp"
                variant="outlined"
                className="w-full"
                autoComplete="no"
                {...field}
              />
            )}
          />
        )}
      </div>
      <div className="mt-5 w-full">
        {isSendOtp ? (
          <Button
            variant="contained"
            className="w-full capitalize"
            onClick={onSubmit}
          >
            Xác thực
          </Button>
        ) : (
          <Button
            variant="contained"
            className="w-full capitalize"
            onClick={onSubmit}
          >
            Gửi
          </Button>
        )}
      </div>
      <div className="mt-5 w-full">
        <Button
          className="text-[#1976d2] decoration-1 cursor-pointer"
          onClick={handleReSendOTP}
        >
          Gửi lại OTP
        </Button>
      </div>
    </>
  );
};

export default ForgetPassword;
