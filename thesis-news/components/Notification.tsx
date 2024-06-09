import { Snackbar } from "@mui/material";

type IPropsNotification = {
    open: boolean
    message: string
    handleCloseNoti: () => void
}
const Notification: React.FC<IPropsNotification> = ({ open, message, handleCloseNoti }) => {
    
  return (
    <>
      <Snackbar
        open={open}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        autoHideDuration={6000}
        onClose={handleCloseNoti}
        message={message}
      />
    </>
  );
};

export default Notification;
