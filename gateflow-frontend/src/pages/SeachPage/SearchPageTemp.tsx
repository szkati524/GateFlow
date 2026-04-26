import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './SearchPage.module.css'; 
import Header from "../../components/Common/Header";
import { apiFetch } from "../../api";

interface SearchParams {
    reg: string; name: string; surname: string;
    company: string; brand: string;
    entryDate: string; entryTime: string; exitTime: string;
}

const SearchPage = () => {
    const navigate = useNavigate();
    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [results, setResults] = useState<any[]>([]);
    const [params, setParams] = useState<SearchParams>({
        reg: '', name: '', surname: '', company: '', brand: '', 
        entryDate: '', entryTime: '', exitTime: ''
    });

    const handleSearch = async () => {
        const queryParams = new URLSearchParams(
            Object.entries(params).filter(([_, v]) => v !== '') as any
        );
        try {
            const res = await apiFetch(`/api/visits/search?${queryParams.toString()}`);
            if (!res.ok) throw new Error(`Błąd: ${res.status}`);
            const data = await res.json();
            setResults(Array.isArray(data) ? data : []);
        } catch (error) {
            console.error("Błąd:", error);
            setResults([]);
        }
    };


    const handleLogout = () => {
      
        localStorage.removeItem("token"); 
       
        localStorage.removeItem("user");
      
        navigate('/login');
    };

    return (
         <div className={styles.wrapper}>
            <Header />

            <div className={styles.topControls}>
                <button className={styles.menuTrigger} onClick={toggleSidebar}>
                    {isSidebarOpen ? '✕' : '☰'}
                </button>
                <div className={styles.rightActions}>
                    <button className={styles.iconBtn}>⚙️</button>
                    <button className={styles.logoutBtn} onClick={handleLogout}>Wyloguj się</button>
                </div>
            </div>

<aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
    <nav className={styles.navMenu}>
        
        <a onClick={() => navigate('/')}>Ruch pojazdów</a>
        <a onClick={() => navigate('/add-entry')}>Dodaj wjazd</a>
        <a onClick={() => navigate('/search')}>Wyszukaj</a>
        <a onClick={() => navigate('/raports')}>Raporty</a>

       
 
    </nav>
</aside>
            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.listSection}>
                    <h2>Wyszukiwanie wizyt</h2>
                    <div className={styles.searchForm}>
                        <div className={styles.formGroup}><label>Nr Rejestracyjny</label><input onChange={e => setParams({...params, reg: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Imię kierowcy</label><input onChange={e => setParams({...params, name: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Nazwisko</label><input onChange={e => setParams({...params, surname: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Firma</label><input onChange={e => setParams({...params, company: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Marka</label><input onChange={e => setParams({...params, brand: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Data wjazdu</label><input type="date" onChange={e => setParams({...params, entryDate: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Godz. wjazdu</label><input type="time" onChange={e => setParams({...params, entryTime: e.target.value})} /></div>
                        <div className={styles.formGroup}><label>Godz. wyjazdu</label><input type="time" onChange={e => setParams({...params, exitTime: e.target.value})} /></div>
                        
                        <button className={styles.searchBtn} onClick={handleSearch}>Szukaj</button>
                    </div>

                    <table className={styles.vehicleTable}>
                        <thead><tr><th>Nr Rej</th><th>Imię</th><th>Nazwisko</th><th>Firma</th><th>Wjazd</th></tr></thead>
                        <tbody>
                            {results.length > 0 ? results.map((v: any) => (
                                <tr key={v.id}>
                                    <td>{v.registrationNumber}</td>
                                    <td>{v.driverFullName?.split(' ')[0]}</td>
                                    <td>{v.driverFullName?.split(' ')[1] || ''}</td>
                                    <td>{v.companyName}</td>
                                    <td>{v.entryTime ? new Date(v.entryTime).toLocaleTimeString() : '-'}</td>
                                </tr>
                            )) : <tr><td colSpan={5}>Brak wyników</td></tr>}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
};
export default SearchPage;