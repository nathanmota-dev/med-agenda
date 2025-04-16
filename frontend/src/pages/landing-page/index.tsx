import { Stethoscope, UserCog, User } from "lucide-react"
import { Button } from "../../components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle
} from "../../components/ui/card"

export default function LandingPage() {
    return (
        <div className="min-h-screen bg-white">            
            
            <div className="relative h-[23rem] w-full">
                <img
                    src="/hero.jpg"
                    alt="Imagem de destaque Med Agenda"
                    className="absolute inset-0 h-full w-full object-cover"
                />
                <div className="absolute inset-0 bg-black/40 flex items-center justify-center">
                    <div className="text-center text-white px-4">
                        <h1 className="text-4xl md:text-5xl font-bold mb-4 drop-shadow-lg">
                            Med Agenda
                        </h1>
                        <p className="text-lg md:text-xl max-w-xl mx-auto drop-shadow-md">
                            Uma solução digital completa para clínicas modernas. Agendamentos mais rápidos, dados seguros e experiência otimizada para todos.
                        </p>
                    </div>
                </div>
            </div>
            
            <div className="container mx-auto px-4 pt-16">
                <div className="mb-12 text-center">
                    <h2 className="mb-4 text-3xl font-semibold text-blue-600 md:text-4xl">
                        Sistema de Gerenciamento de Consultas Médicas
                    </h2>
                    <p className="mx-auto max-w-2xl text-lg text-gray-600">
                        Bem-vindo ao Med Agenda. Por favor, selecione o tipo de usuário para continuar.
                    </p>
                </div>

                <div className="grid gap-8 md:grid-cols-3">                    
                    <Card className="flex flex-col transition-all hover:border-blue-600 hover:shadow-md">
                        <CardHeader className="text-center">
                            <div className="mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-blue-100">
                                <UserCog className="h-10 w-10 text-blue-600" />
                            </div>
                            <CardTitle className="text-xl text-blue-600">Administrador</CardTitle>
                            <CardDescription>Acesso para gerenciamento do sistema</CardDescription>
                        </CardHeader>
                        <CardContent className="flex-grow">
                            <p className="text-gray-600">
                                Gerencie médicos, pacientes, consultas e tenha acesso a todas as funcionalidades administrativas.
                            </p>
                        </CardContent>
                        <CardFooter>
                            <a href="/admin/login" className="w-full">
                                <Button className="w-full bg-blue-600 hover:bg-blue-700">
                                    Entrar como Administrador
                                </Button>
                            </a>
                        </CardFooter>
                    </Card>
                    
                    <Card className="flex flex-col transition-all hover:border-blue-600 hover:shadow-md">
                        <CardHeader className="text-center">
                            <div className="mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-blue-100">
                                <Stethoscope className="h-10 w-10 text-blue-600" />
                            </div>
                            <CardTitle className="text-xl text-blue-600">Médico</CardTitle>
                            <CardDescription>Acesso para profissionais de saúde</CardDescription>
                        </CardHeader>
                        <CardContent className="flex-grow">
                            <p className="text-gray-600">
                                Visualize sua agenda, gerencie consultas e acesse prontuários de pacientes.
                            </p>
                        </CardContent>
                        <CardFooter>
                            <a href="/doctor/login" className="w-full">
                                <Button className="w-full bg-blue-600 hover:bg-blue-700">
                                    Entrar como Médico
                                </Button>
                            </a>
                        </CardFooter>
                    </Card>
                    
                    <Card className="flex flex-col transition-all hover:border-blue-600 hover:shadow-md">
                        <CardHeader className="text-center">
                            <div className="mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-blue-100">
                                <User className="h-10 w-10 text-blue-600" />
                            </div>
                            <CardTitle className="text-xl text-blue-600">Paciente</CardTitle>
                            <CardDescription>Acesso para pacientes</CardDescription>
                        </CardHeader>
                        <CardContent className="flex-grow">
                            <p className="text-gray-600">
                                Agende consultas, visualize seu histórico médico e mantenha seus dados atualizados.
                            </p>
                        </CardContent>
                        <CardFooter>
                            <a href="/patient/login" className="w-full">
                                <Button className="w-full bg-blue-600 hover:bg-blue-700">
                                    Entrar como Paciente
                                </Button>
                            </a>
                        </CardFooter>
                    </Card>
                </div>

                <footer className="mt-16 text-center text-sm text-gray-500 pb-6">
                    <p>
                        © {new Date().getFullYear()} Med Agenda. Todos os direitos reservados.
                    </p>
                </footer>
            </div>
        </div>
    )
}
