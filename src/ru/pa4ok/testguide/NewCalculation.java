package ru.pa4ok.testguide;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *  это самое костыльное и одновременно самое короткое и понятное решение
 */
public class NewCalculation
{
    public static void main(String[] args)
    {
        NewCalculation calculation = new NewCalculation();

        LocalTime[] startTimes = new LocalTime[] {
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 50)
        };

        int[] durations = { 60, 30, 10, 10, 40 };

        String[] array = calculation.getPeriods(startTimes, durations, 30, LocalTime.of(8, 0), LocalTime.of(18, 0));

        for(String s : array) {
            System.out.println(s);
        }
    }

    public String[] getPeriods(LocalTime[] startTimes, int[] durations, int consultationTimeLocalTime, LocalTime beginWorkingTime, LocalTime endWorkingTime)
    {
        //формат для времени
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

        //список строк
        //в него мы поместим все минуты от начала до конца рабочего дня
        List<String> minutesList = new ArrayList<>();

        //цикл от первого до последнего часа
        for(int i=beginWorkingTime.getHour(); i<=endWorkingTime.getHour(); i++) {
            //цикл от 0 ло 59 минут
            for(int j=0; j<60; j++) {
                //получаем объект LocalTime из часа и минуты в цикле
                LocalTime time = LocalTime.of(i, j);
                //если время >= начала рабочего дня и <= конца рабочего дня
                if(time.equals(beginWorkingTime) || time.isAfter(beginWorkingTime) && time.equals(endWorkingTime) || time.isBefore(endWorkingTime)) {
                    //форматируем и добавляем в список
                    minutesList.add(time.format(format));
                }
            }
        }

        //перебираем все занятые минуты и выставляем на их место null
        for(int i=0; i<startTimes.length; i++) {
            LocalTime start = startTimes[i];
            for(int j=1; j<durations[i]; j++) {
                minutesList.set(minutesList.indexOf(start.plusMinutes(j).format(format)), null);
            }
        }

        //список с финальными отрезками
        List<String> finalList = new ArrayList<>();

        String startMinute = null;
        int counter = 0;

        //ищем отрезки без null длиной consultationTimeLocalTime
        for(String minute : minutesList) {
            if(minute == null) {
                counter = 0;
                continue;
            }

            if(counter == 0) {
                startMinute = minute;
            } else if(counter == consultationTimeLocalTime) {
                finalList.add(startMinute + "-" + minute);
                startMinute = minute;
                counter = 1;
                continue;
            }

            counter++;
        }

        //переводим список в массив
        return finalList.toArray(new String[0]);
    }
}