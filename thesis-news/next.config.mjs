/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "i1-vnexpress.vnecdn.net",
        port: "",
        pathname: "/**",
      },
    ],
  },
};

export default nextConfig;
