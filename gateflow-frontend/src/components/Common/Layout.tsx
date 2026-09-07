
import { useState, ReactNode } from "react";
import Header from "./Header";
import Navbar from "./Navbar";
import styles from "./Layout.module.css";

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  return (
    <div className={styles.wrapper}>
      <Header />
      <Navbar onToggleSidebar={(isOpen) => setIsSidebarOpen(isOpen)} />
      
      <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ""}`}>
        {children}
      </main>
    </div>
  );
};

export default Layout;