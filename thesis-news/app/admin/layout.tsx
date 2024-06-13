
import AppBarAdmin from "@/components/AppBarAdmin";
import Sidebar from "@/components/Sidebar";
import { HeightDefault, ROLE } from "@/constant";
import {
  Box,
  CssBaseline,
  StyledEngineProvider,
  ThemeProvider,
} from "@mui/material";

export default function Layout({ children }: any) {
  return (
    <>
      <StyledEngineProvider injectFirst>
        <ThemeProvider theme={[]}>
          <Box sx={{ display: "flex" }}>
            <CssBaseline />
            <AppBarAdmin position="absolute" open />
            <Sidebar
              open
              // onLogout={handleLogout}
              // toggleDrawer={toggleDrawer}
              roleAssign={ROLE}
            />
            <main
              style={{ height: `calc(100vh - ${HeightDefault})` }}
              className={`w-full overflow-y-auto bg-f5 p-5 mt-[46px]`}
            >
              {children}
            </main>
          </Box>
        </ThemeProvider>
      </StyledEngineProvider>
    </>
  );
}
