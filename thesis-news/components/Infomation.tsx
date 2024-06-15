import EditIcon from "@mui/icons-material/Edit";
import { Divider } from "@mui/joy";
import { useState } from "react";
import DoneIcon from "@mui/icons-material/Done";
import { Controller, useForm } from "react-hook-form";
import {
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  TextField,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import CancelIcon from "@mui/icons-material/Cancel";
import { adminNotify, notifyType } from "@/lib/format";

const Infomation: React.FC<{ name: string; email: string }> = ({
  name,
  email,
}) => {
  const { handleSubmit, reset, control } = useForm<{
    password: string;
    name: string;
    oldPassword: string;
  }>({
    defaultValues: {
      password: "",
      name: "",
      oldPassword: "",
    },
  });

  const [dataName, setDataName] = useState(name);
  const [isName, setIsName] = useState(false);
  const [isPassword, setIsPassword] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleChangeName = (val: boolean) => {
    setIsName(val);
  };
  const handleChangePassword = (val: boolean) => {
    setIsPassword(val);
  };

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const onSubmit = handleSubmit(async (data) => {
    const memberId = JSON.parse(localStorage.getItem("user") as any);
    const resp = await fetch("http://localhost:8080/user/update", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        fullName: data.name,
        memberId: memberId?.member?.id,
        updateMemberId: memberId?.member?.id,
      }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp?.statusCode !== 200) {
      adminNotify(resp?.message || resp?.data?.message, notifyType.ERROR);
    }
    if (resp?.statusCode === 200) {
      adminNotify(resp?.message || resp?.data?.message, notifyType.SUCCESS);
      setDataName(resp?.data?.member?.fullName);
      localStorage.setItem("user", JSON.stringify(resp?.data));
    }
    setIsName(false);
    reset();
  });

  const onSubmitPassword = handleSubmit(async (data) => {
    const email = JSON.parse(localStorage.getItem("user") as any);
    const resp = await fetch("http://localhost:8080/user/change-password", {
      method: "POST",
      mode: "cors",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email?.account?.email || "",
        oldPassword: data.oldPassword,
        password: data.password,
      }),
    })
      .then((result) => result.json())
      .catch((e) => console.log(e));

    if (resp?.statusCode !== 200) {
      adminNotify(resp?.message || resp?.data?.message, notifyType.ERROR);
    }
    if (resp?.statusCode === 200) {
      console.log(resp?.message || resp?.data?.message);
      adminNotify(resp?.message || resp?.data?.message, notifyType.SUCCESS);
    }
    setIsPassword(false);
    reset();
  });

  return (
    <div>
      <div className="block">
        <div className="flex flex-col">
          <div className="mb-4">
            <h1 className="font-bold text-2xl">Thông tin tài khoản</h1>
          </div>
          {name && (
            <>
              <div className="flex flex-col mb-2">
                <div className="flex justify-between">
                  <label className="font-semibold pb-2">Tên: </label>
                  <label className="cursor-pointer">
                    {isName ? (
                      <>
                        <DoneIcon onClick={onSubmit} />
                        <CancelIcon onClick={() => handleChangeName(false)} />
                      </>
                    ) : (
                      <EditIcon onClick={() => handleChangeName(true)} />
                    )}
                  </label>
                </div>
                {isName ? (
                  <Controller
                    name="name"
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        label="Name"
                        variant="outlined"
                        className="w-full"
                        autoComplete="off" // Ngăn trình duyệt tự động điền
                      />
                    )}
                  />
                ) : (
                  <span>{dataName || name || ""}</span>
                )}
              </div>
              <Divider component="li" className="my-4 mb-2" />
            </>
          )}
          <div className="flex flex-col mb-2">
            <div className="flex justify-between">
              <label className="font-semibold pb-2">Email: </label>
            </div>
            <span>{email}</span>
          </div>
          <Divider component="li" className="my-4 mb-2" />
          <div className="flex flex-col mb-2">
            <div className="flex justify-between">
              <label className="font-semibold pb-2">Mật khẩu: </label>
              <label className="cursor-pointer">
                {isPassword ? (
                  <>
                    <DoneIcon onClick={onSubmitPassword} />
                    <CancelIcon onClick={() => handleChangePassword(false)} />
                  </>
                ) : (
                  <EditIcon onClick={() => handleChangePassword(true)} />
                )}
              </label>
            </div>
            {isPassword ? (
              <>
                <Controller
                  name="oldPassword"
                  control={control}
                  render={({ field }) => (
                    <FormControl className="w-full mt-6" variant="outlined">
                      <InputLabel htmlFor="outlined-adornment-passwordss">
                        Mật khẩu cũ
                      </InputLabel>
                      <OutlinedInput
                        {...field}
                        id="outlined-adornment-passwordss"
                        type={showPassword ? "text" : "password"}
                        endAdornment={
                          <InputAdornment position="end">
                            <IconButton
                              aria-label="toggle password visibility"
                              onClick={handleClickShowPassword}
                              edge="end"
                            >
                              {showPassword ? (
                                <VisibilityOff />
                              ) : (
                                <Visibility />
                              )}
                            </IconButton>
                          </InputAdornment>
                        }
                        label="Mật khẩu cũ"
                        autoComplete="new-password"
                      />
                    </FormControl>
                  )}
                />
                <Controller
                  name="password"
                  control={control}
                  render={({ field }) => (
                    <FormControl className="w-full mt-6" variant="outlined">
                      <InputLabel htmlFor="outlined-adornment-passwordss">
                        Mật khẩu mới
                      </InputLabel>
                      <OutlinedInput
                        {...field}
                        id="outlined-adornment-passwordss"
                        type={showPassword ? "text" : "password"}
                        endAdornment={
                          <InputAdornment position="end">
                            <IconButton
                              aria-label="toggle password visibility"
                              onClick={handleClickShowPassword}
                              edge="end"
                            >
                              {showPassword ? (
                                <VisibilityOff />
                              ) : (
                                <Visibility />
                              )}
                            </IconButton>
                          </InputAdornment>
                        }
                        label="Mật khẩu mới"
                        autoComplete="new-password"
                      />
                    </FormControl>
                  )}
                />
              </>
            ) : (
              <span>******</span>
            )}
          </div>
          <Divider component="li" className="my-4 mb-2" />
        </div>
      </div>
    </div>
  );
};

export default Infomation;
