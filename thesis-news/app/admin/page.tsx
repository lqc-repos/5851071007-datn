import TableCustom from "@/components/Table";
import { Box, Container, CssBaseline, StyledEngineProvider } from "@mui/material";

const Admin: React.FC = () => {
  return (
    <>
      <StyledEngineProvider injectFirst>
        <Box sx={{ display: "flex" }}>
          <CssBaseline />

          {/* <AdminLayout appRouter={router} isMobile={pageProps?.isMobile}>
            <Component {...pageProps} />
          </AdminLayout> */}
        </Box>
      </StyledEngineProvider>
      {/* <Box sx={{ display: "flex" }}>
        <div className="block flex-auto w-1/5">nav</div>
        <div className="block flex-auto w-4/5">
          <TableCustom />
        </div>
      </Box> */}
    </>
  );
};

export default Admin;
