import { Box, CssBaseline, StyledEngineProvider } from "@mui/material";

const Admin: React.FC = () => {
  return (
    <div>
      <StyledEngineProvider injectFirst>
        <Box sx={{ display: "flex" }}>
          <CssBaseline />
        </Box>
      </StyledEngineProvider>
    </div>
  );
};

export default Admin;
