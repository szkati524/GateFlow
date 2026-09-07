import { useNavigate } from "react-router-dom";
import styles from './Navbar.module.css'; 

interface NavbarProps {
    isSidebarOpen: boolean;
    onToggleSidebar: () => void;
}

const Navbar = ({ isSidebarOpen, onToggleSidebar }: NavbarProps) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate('/login');
    };

    return (
        <>
           
            <header className={styles.topBar}>
                <button className={styles.menuTrigger} onClick={onToggleSidebar}>
                    {isSidebarOpen ? '✕' : '☰'}
                </button>
                
                <div className={styles.rightActions}>
                    <button className={styles.iconBtn} onClick={() => navigate('/options')}>⚙️</button>
                    <button className={styles.logoutBtn} onClick={handleLogout}>Wyloguj się</button>
                </div>
            </header>

          
            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a onClick={() => navigate("/")}>Ruch pojazdów</a>
                    <a onClick={() => navigate("/add-entry")}>Dodaj wjazd</a>
                    <a onClick={() => navigate("/search")}>Wyszukaj</a>
                    <a onClick={() => navigate("/raport")}>Raporty</a>
                </nav>
            </aside>
        </>
    );
};

export default Navbar;