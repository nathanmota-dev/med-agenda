"use client"

import { useState } from "react"
import { Button } from "../ui/button"
import { Input } from "../ui/input"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "../ui/card"
import { MessageCircle, X, Send, Bot, User } from "lucide-react"
import { ScrollArea } from "../ui/scroll-area"

type Message = {
    id: string
    role: "user" | "assistant"
    content: string
}

export default function ChatAI() {
    const [isOpen, setIsOpen] = useState(false)
    const [input, setInput] = useState("")
    const [messages, setMessages] = useState<Message[]>([
        {
            id: "1",
            role: "assistant",
            content: "Olá! Sou seu assistente de IA. Como posso ajudá-lo hoje?",
        },
    ])
    const [isLoading, setIsLoading] = useState(false)
    const [streamingMessage, setStreamingMessage] = useState("")

    const toggleChat = () => setIsOpen(!isOpen)

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        if (!input.trim()) return

        const userMessage: Message = {
            id: crypto.randomUUID(),
            role: "user",
            content: input,
        }

        setMessages((prev) => [...prev, userMessage])
        setInput("")
        setIsLoading(true)
        setStreamingMessage("")

        const res = await fetch("http://localhost:11434/api/generate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                model: "llama3.2",
                prompt: input,
                stream: true,
            }),
        })

        const reader = res.body?.getReader()
        const decoder = new TextDecoder("utf-8")
        let fullResponse = ""

        while (true) {
            const { value, done } = await reader!.read()
            if (done) break

            const chunk = decoder.decode(value)
            const lines = chunk.split("\n")

            for (const line of lines) {
                if (!line.trim()) continue

                try {
                    const json = JSON.parse(line)
                    if (json.response) {
                        fullResponse += json.response
                        setStreamingMessage(fullResponse)
                    }
                } catch (err) {
                    console.error("Erro no JSON:", err)
                }
            }
        }

        setMessages((prev) => [
            ...prev,
            {
                id: crypto.randomUUID(),
                role: "assistant",
                content: fullResponse,
            },
        ])

        setStreamingMessage("")
        setIsLoading(false)
    }

    return (
        <>
            {/* Botão flutuante */}
            <div className="fixed bottom-6 right-6 z-50">
                <Button
                    onClick={toggleChat}
                    size="lg"
                    className="h-14 w-14 rounded-full shadow-lg hover:shadow-xl transition-all duration-200 bg-blue-600 hover:bg-blue-700"
                >
                    {isOpen ? <X className="h-6 w-6" /> : <MessageCircle className="h-6 w-6" />}
                </Button>
            </div>

            {/* Chat Modal */}
            {isOpen && (
                <div className="fixed bottom-24 right-6 z-40 w-80 sm:w-96">
                    <Card className="shadow-2xl border-0 bg-white">
                        <CardHeader className="bg-blue-600 text-white rounded-t-lg">
                            <CardTitle className="flex items-center gap-2 text-lg">
                                <Bot className="h-5 w-5" />
                                Assistente IA
                            </CardTitle>
                        </CardHeader>

                        <CardContent className="p-0">
                            <ScrollArea className="h-80 p-4">
                                <div className="space-y-4">
                                    {messages.map((message) => (
                                        <div
                                            key={message.id}
                                            className={`flex items-start gap-2 ${message.role === "user" ? "flex-row-reverse" : "flex-row"}`}
                                        >
                                            <div
                                                className={`flex-shrink-0 w-8 h-8 rounded-full flex items-center justify-center ${message.role === "user" ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-600"
                                                    }`}
                                            >
                                                {message.role === "user" ? <User className="h-4 w-4" /> : <Bot className="h-4 w-4" />}
                                            </div>

                                            <div
                                                className={`max-w-[70%] p-3 rounded-lg ${message.role === "user"
                                                        ? "bg-blue-600 text-white rounded-br-none"
                                                        : "bg-gray-100 text-gray-800 rounded-bl-none"
                                                    }`}
                                            >
                                                <p className="text-sm whitespace-pre-wrap">{message.content}</p>
                                            </div>
                                        </div>
                                    ))}

                                    {streamingMessage && (
                                        <div className="flex items-start gap-2">
                                            <div className="flex-shrink-0 w-8 h-8 rounded-full bg-gray-200 text-gray-600 flex items-center justify-center">
                                                <Bot className="h-4 w-4" />
                                            </div>
                                            <div className="bg-gray-100 text-gray-800 p-3 rounded-lg rounded-bl-none">
                                                <p className="text-sm whitespace-pre-wrap">{streamingMessage}</p>
                                            </div>
                                        </div>
                                    )}

                                    {isLoading && !streamingMessage && (
                                        <div className="flex items-start gap-2">
                                            <div className="flex-shrink-0 w-8 h-8 rounded-full bg-gray-200 text-gray-600 flex items-center justify-center">
                                                <Bot className="h-4 w-4" />
                                            </div>
                                            <div className="bg-gray-100 text-gray-800 p-3 rounded-lg rounded-bl-none">
                                                <div className="flex space-x-1">
                                                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                                                    <div
                                                        className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"
                                                        style={{ animationDelay: "0.1s" }}
                                                    ></div>
                                                    <div
                                                        className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"
                                                        style={{ animationDelay: "0.2s" }}
                                                    ></div>
                                                </div>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </ScrollArea>
                        </CardContent>

                        <CardFooter className="p-4 border-t">
                            <form onSubmit={handleSubmit} className="flex w-full gap-2">
                                <Input
                                    value={input}
                                    onChange={(e) => setInput(e.target.value)}
                                    placeholder="Digite sua mensagem..."
                                    className="flex-1"
                                    disabled={isLoading}
                                />
                                <Button
                                    type="submit"
                                    size="sm"
                                    disabled={isLoading || !input.trim()}
                                    className="bg-blue-600 hover:bg-blue-700"
                                >
                                    <Send className="h-4 w-4" />
                                </Button>
                            </form>
                        </CardFooter>
                    </Card>
                </div>
            )}
        </>
    )
}
