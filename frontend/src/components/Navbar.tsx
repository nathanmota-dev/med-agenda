export default function Navbar() {
    return (
        <header className="bg-blue-600 shadow-md">
            <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
                <h2 className="text-2xl font-bold text-white">Med Agenda</h2>
                <div className="flex items-center">
                    <span className="text-white mr-4">Usuário</span>
                    <div className="h-10 w-10 rounded-full bg-white flex items-center justify-center text-blue-600 font-bold">
                        US
                    </div>
                </div>
            </div>
        </header>
    );
}
