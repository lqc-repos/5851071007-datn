import { adminNotify, notifyType } from "@/lib/format";
import { Button, TextField } from "@mui/material";
import { useState } from "react";
import { useForm, Controller } from "react-hook-form";

interface IFormInputs {
  email: string;
  otp: string;
}

const ForgetPassword: React.FC<{
  handleChangeNewPassword: (e: string) => void;
}> = ({ handleChangeNewPassword}) => {
  const { handleSubmit, control } = useForm<IFormInputs>({
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
      adminNotify(resp?.message || resp?.data?.message, notifyType.ERROR);
      return;
    }

    if (resp?.statusCode === 200) {
      adminNotify(resp?.message || resp?.data?.message, notifyType.SUCCESS);
      return true;
    }
  };

  const onSubmit = async (data: IFormInputs) => {
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
      adminNotify(resp?.message || resp?.data?.message, notifyType.ERROR);
    }
    if (resp.statusCode === 200) {
      adminNotify(resp?.message || resp?.data?.message, notifyType.SUCCESS);
      handleChangeNewPassword(data.email);
      return;
    }
  };

  const handleReSendOTP = handleSubmit((data) => {
    sendOtp(data);
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
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
            // onClick={onSubmit}
            type="submit"
          >
            Xác thực
          </Button>
        ) : (
          <Button
            variant="contained"
            className="w-full capitalize"
            // onClick={onSubmit}
            type="submit"
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
    </form>
  );
};

export default ForgetPassword;
