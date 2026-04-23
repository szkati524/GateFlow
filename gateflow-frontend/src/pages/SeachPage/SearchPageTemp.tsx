import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './SearchPage.module.css'; 
import Header from "../../components/Common/Header";

interface SearchParams {
    reg: string;
    name: string;
    surname: string;
    company: string;
    brand: string;
    exitTime: string;
}

const SearchPage = () => {
    const navigate = useNavigate();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [results, setResults] = useState<any[]>([]);
    const [params, setParams] = useState<SearchParams>({
        reg: '', name: '', surname: '', company: '', brand: '', exitTime: ''
    });

    const handleSearch = async () => {
        // Budowanie parametrów zapytania
        const queryParams = new URLSearchParams(
            Object.entries(params).filter(([_, v]) => v !== '') as any
        );

        try {
            const res = await fetch(`/api/visits/search?${queryParams.toString()}`);
            
            if (!res.ok) {
                throw new Error(`Błąd serwera: ${res.status}`);
            }

            const data = await res.json();
           
            setResults(Array.isArray(data) ? data : []);
        } catch (error) {
            console.error("Błąd wyszukiwania:", error);
            setResults([]); 
        }
    };

    return (
        <div className={styles.wrapper}>
            <Header />
            
            <button className={styles.menuTrigger} onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
                {isSidebarOpen ? '✕' : '☰'}
            </button>

            <div className={styles.rightActions}>
                <button className={styles.iconBtn}>⚙️</button>
                <button className={styles.logoutBtn} onClick={() => navigate('/login')}>Wyloguj się</button>
            </div>

            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a onClick={() => navigate('/')}>Ruch pojazdów</a>
                    <a onClick={() => navigate('/add-entry')}>Dodaj wjazd</a>
                    <a onClick={() => navigate('/search')}>Wyszukaj</a>
                    <a href="#Raport">Raporty</a>
                </nav>
            </aside>

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.listSection}>
                    <h2>Wyszukiwanie wizyt</h2>
                    
                    <div className={styles.searchForm}>
                        <input placeholder="Nr Rejestracyjny" onChange={e => setParams({...params, reg: e.target.value})} />
                        <input placeholder="Imię kierowcy" onChange={e => setParams({...params, name: e.target.value})} />
                        <input placeholder="Nazwisko" onChange={e => setParams({...params, surname: e.target.value})} />
                        <input placeholder="Firma" onChange={e => setParams({...params, company: e.target.value})} />
                        <input placeholder="Marka" onChange={e => setParams({...params, brand: e.target.value})} />
                        
                        <label style={{ color: '#888', fontSize: '12px' }}>Data wyjazdu:</label>
                        <input type="date" onChange={e => setParams({...params, exitTime: e.target.value})} />
                        
                        <button className={styles.searchBtn} onClick={handleSearch}>Szukaj</button>
                    </div>

                    <table className={styles.vehicleTable}>
                        <thead>
                            <tr><th>Nr Rej</th><th>Imię</th><th>Nazwisko</th><th>Firma</th><th>Wjazd</th></tr>
                        </thead>
                        <tbody>
                            
                            {results.length > 0 ? (
                                results.map((v: any) => (
                                    <tr key={v.id}>
                                        <td>{v.registrationNumber}</td>
                                        <td>{v.driverFullName?.split(' ')[0]}</td>
                                        <td>{v.driverFullName?.split(' ')[1] || ''}</td>
                                        <td>{v.companyName}</td>
                                        <td>{v.entryTime ? new Date(v.entryTime).toLocaleTimeString() : '-'}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={5} style={{ padding: '20px', color: '#666' }}>Brak wyników do wyświetlenia</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
};

export default SearchPage;