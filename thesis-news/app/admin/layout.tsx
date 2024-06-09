import AppBarAdmin from "@/components/AppBarAdmin";
import { ROLE } from "@/constant";

export default function AdminLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <AppBarAdmin
          position="absolute"
          open
          //   toggleDrawer={toggleDrawer}
        />
        <main
          style={{ height: `calc(100vh - 46px)` }}
          className={`w-full overflow-y-auto bg-f5`}
        >
          {children}
        </main>
      </body>
    </html>
  );
}
